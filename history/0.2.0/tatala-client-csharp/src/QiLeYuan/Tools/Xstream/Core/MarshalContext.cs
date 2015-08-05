using System;
using System.Collections;
using System.Collections.Generic;
using System.Reflection;
using System.Xml;
using Xstream.Core.Converters;
using Xstream.Core.Converters.Complex;

namespace Xstream.Core
{
    /// <summary>
    /// A MarshalContext provides methods and a context enviroment
    /// to convert objects to XML, or convert XML back into the original
    /// (or different) objects.
    /// </summary>
    public class MarshalContext : IMarshalContext
    {
        private static readonly Assembly __mscorlib = typeof (int).Assembly;
        private static readonly Type __objectType = typeof (object);
        private static readonly Type __typeType = typeof (Type);
        private static readonly Type __arrayType = typeof (Array);
        private static readonly Type __genericListType = typeof(List<>);
        private static readonly Type __enumType = typeof (Enum);
        private static readonly Type __nullType = typeof (NullType);
        private static readonly Type __methodInfoType = typeof (MethodInfo);

        private readonly Hashtable converterMap = new Hashtable(20);
        private readonly Hashtable aliasMap = new Hashtable();
        private readonly Hashtable reverseAliasMap = new Hashtable();
        private readonly Hashtable cdataMap = new Hashtable();

        private readonly ArrayList stackList = new ArrayList();
        private readonly Hashtable stackMap;
        private bool useRepository;
        private Type ignoredAttributeType;
        private bool caseSensitive;

        /// <summary>
        /// Registers a set of default converters for almost all types.
        /// </summary>
        public MarshalContext() : this(new ReferenceComparer()) {}

        public MarshalContext(IEqualityComparer equalityComparer)
        {
            stackMap = new Hashtable(equalityComparer);
            useRepository = false;
            caseSensitive = true;
            AddConverters();
        }

        private void AddConverters()
        {
            AddConverter(new NullConverter());
            AddConverter(new IntConverter());
            AddConverter(new LongConverter());
            AddConverter(new ShortConverter());
            AddConverter(new FloatConverter());
            AddConverter(new DoubleConverter());
            AddConverter(new StringConverter());
            AddConverter(new BooleanConverter());
            AddConverter(new CharConverter());
            AddConverter(new ObjectConverter());
            AddConverter(new ArrayConverter());
            AddConverter(new PointerConverter());
            AddConverter(new ByteConverter());
            AddConverter(new DateTimeConverter());
            AddConverter(new TimeSpanConverter());
            AddConverter(new DecimalConverter());
            AddConverter(new GuidConverter());
            AddConverter(new EnumConverter());
            AddConverter(new TypeConverter());
            AddConverter(new MethodInfoConverter());
            AddConverter(new StringBuilderConverter());
            AddConverter(new CDataConverter());
            AddConverter(new GenericListConverter());
        }

        public bool UseRepository
        {
            get { return useRepository; }
            set { useRepository = value; }
        }

        public bool CaseSensitive
        {
            get { return caseSensitive; }
            set { caseSensitive = value; }
        }

        public virtual void AddConverter(IConverter converter)
        {
            converter.Register(this);
        }

        public virtual bool IsCData(Type type, string fieldName)
        {
            if (cdataMap.Contains(type) 
                && ((ArrayList)cdataMap[type]).Contains(fieldName)) 
                return true;
            return false;
        }

        public virtual IConverter GetCDataConverter()
        {
            return (IConverter) converterMap[typeof (CDataConverter)];
        }

        public void AddCdata(Type type, string name)
        {
            if(!cdataMap.Contains(type))
            {
                ArrayList fields = new ArrayList();
                fields.Add(name);
                cdataMap.Add(type, fields);
            }
            else
            {
                ArrayList fields = (ArrayList)cdataMap[type];
                if (!fields.Contains(name))
                    fields.Add(name);
            }
        }

        /// <summary>
        /// Registers a new converter for a specific type.
        /// </summary>
        /// <param name="type">Type the converter can handle.</param>
        /// <param name="converter">Converter instance to use when converting.</param>
        public virtual void RegisterConverter(Type type, IConverter converter)
        {
            if (type == null)
                type = __nullType;

            converterMap[type] = converter;
        }

        /// <summary>
        /// Adds a simple string alias for a specific Type.
        /// </summary>
        /// <param name="alias">String alias name.</param>
        /// <param name="type">Type to use the alias for.</param>
        public void Alias(string alias, Type type)
        {
            // Perform a regular expression check on the alias name:
            // only _ and alphanumerical characters are allowed

            if (type == null)
                type = __nullType;

            // Add alias to forward and reverse map
            aliasMap[alias] = type;
            reverseAliasMap[type] = alias;
        }

        /// <summary>
        /// Returns the name of the type in the shortest safe way that 
        /// it can be used to retrieve the Type instance.
        /// </summary>
        public string GetTypeName(Type type)
        {
            if (type.Assembly == __mscorlib)
                return type.FullName;

            return type.AssemblyQualifiedName;
        }

        /// <summary>
        /// Writes the opening (start) tag of an element representing
        /// the XML serialized version of an object.
        /// </summary>
        /// <param name="type">Type of the object.</param>
        /// <param name="field">Field of the parent the object represents.</param>
        /// <param name="xml">XML output to write on.</param>
        public void WriteStartTag(Type type, FieldInfo field, XmlTextWriter xml)
        {
            string tagName, assemblyName = null, typeName = null;

            if (field != null)
            {
                // If the field is set, the tag name is the field name
                tagName = auto_property_name(field.Name);
                // If the object type is different from the field type, add the type information
                if (type != field.FieldType && field.FieldType.GetGenericTypeDefinition() != typeof(List<>))
                {
                    // Get the aliased version of the type information
                    typeName = reverseAliasMap[type] as string;

                    if (typeName == null)
                        typeName = GetTypeName(type);
                }
            }
            else
            {
                // No field set, the tag name is the full type name
                tagName = reverseAliasMap[type] as string;

                // If no alias is found, check the assembly
                if (tagName == null)
                {
                    tagName = type.ToString();

                    // If the assembly is not the default assembly, write out the attribute
                    if (type.Assembly != __mscorlib)
                        assemblyName = type.Assembly.FullName;
                }
            }

            // Replace all + characters with - in the tag name
            xml.WriteStartElement(tagName.Replace('+', '-').Replace("[]", "--"));

            if (typeName != null)
                xml.WriteAttributeString("type", typeName);
            if (assemblyName != null)
                xml.WriteAttributeString("assembly", assemblyName);
        }

        public static string auto_property_name(string tagName)
        {
            if (tagName.Contains("k__BackingField"))
                tagName = tagName.Replace("<", "").Replace(">k__BackingField", "");
            return tagName;
        }

        /// <summary>
        /// Writes the closing (end) tag of an element representing the
        /// XML serialized version of an object.
        /// </summary>
        /// <param name="type">Type of the object.</param>
        /// <param name="field">Field of the parent the object represents.</param>
        /// <param name="xml">XML output to write on.</param>
        public void WriteEndTag(Type type, FieldInfo field, XmlTextWriter xml)
        {
            xml.WriteEndElement();
        }

        public Type GetTypeFromTag(XmlNode node, bool isField)
        {
            // Replace all - characters with + in the tag name
            string tagName = node.Name.Replace("--", "[]").Replace('-', '+');

            string typeName = node.Attributes["type"] != null ? node.Attributes["type"].Value : null;
            string assemblyName = node.Attributes["assembly"] != null ? node.Attributes["assembly"].Value : null;
            ;

            Type type = null;

            // If a typeName is specified, use this type
            if (typeName != null)
            {
                type = aliasMap[typeName] as Type;
                if (type == null)
                    type = Type.GetType(typeName);
            }

            if (!isField)
            {
                // Check if the tag is an alias
                type = aliasMap[tagName] as Type;

                if (type == null)
                {
                    string fqn = tagName + (assemblyName != null ? "," + assemblyName : "");
                    type = Type.GetType(fqn);
                }
            }

            return type;
        }

        /// <summary>
        /// Gets an IConverter that can convert the given type.
        /// </summary>
        public IConverter GetConverter(Type type)
        {
            if (type == null)
                type = __nullType;

            // Try a primitive match
            IConverter converter = converterMap[type] as IConverter;

            // Try an exact class match
            if (converter == null)
            {
                if (type.IsArray)
                    return converterMap[__arrayType] as IConverter;
                if (type.IsGenericType && type.GetGenericTypeDefinition() == typeof(List<>))
                    return converterMap[__genericListType] as IConverter;
                if (type.IsEnum)
                    return converterMap[__enumType] as IConverter;
                if (type.IsSubclassOf(__typeType))
                    return converterMap[__typeType] as IConverter;
                if (type.IsSubclassOf(__methodInfoType))
                    return converterMap[__methodInfoType] as IConverter;
                if (type.Name.StartsWith("Predicate`1"))
                    return NoopConverter.Instance;

                converter = ObjectConverter;
            }

            if (converter == null)
                throw new ConversionException("No valid converter found for type " + type);

            return converter;
        }

        public virtual ObjectConverter ObjectConverter
        {
            get { return (ObjectConverter) converterMap[__objectType]; }
        }

        /// <summary>
        /// Gets an IConverter that can convert the object represented
        /// by the XML node back to an instance.  The exact type of the
        /// object represented by the node, if possible to determine,
        /// is passed in and out as a reference parameter.
        /// </summary>
        /// <remarks>
        /// Only if the node represents on object linked to a field of a
        /// parent object, is the reference type allowed to be set, in 
        /// all other cases the value should be null.
        /// </remarks>
        /// <param name="node">XmlNode representing the object.</param>
        /// <param name="type">
        /// Proposed type of the object, this would be the type of the 
        /// field the object represents.
        /// </param>
        public IConverter GetConverter(XmlNode node, ref Type type)
        {
            Type nodeType = GetTypeFromTag(node, type != null);

            if (nodeType != null)
                type = nodeType;

            if (type != null)
                return GetConverter(type);
            else
                throw new ConversionException("Could not find the Type of element: " + node.Name);
        }

        /// <summary>
        /// Adds an object to the context stack, allowing serializing
        /// of nested objects with back references.
        /// </summary>
        public void Stack(object oo)
        {
            if (stackMap.Contains(oo)) return;
            int add = stackList.Add(oo);
            stackMap.Add(oo, add);
        }

        public void Stack(object value, Type type, XmlNode xml)
        {
            int add = stackList.Add(value);
            stackMap.Add(value, add);
        }

        public void Stack(object value, Type type)
        {
            if (stackMap.Contains(value)) return;
            int add = stackList.Add(value);
            stackMap.Add(value, add);
        }

        /// <summary>
        /// Gets the index of the object reference in the stack.  
        /// A negative number (-1) is returned if the object is not
        /// yet contained in the context stack.
        /// </summary>
        public int GetStackIndex(object oo)
        {
            if (!stackMap.Contains(oo))
                return -1;

            return (int) stackMap[oo];
        }

        /// <summary>
        /// Returns the object at the given position in the stack.
        /// Null is returned if no object is found.
        /// </summary>
        public object GetStackObject(int position)
        {
            if (position >= stackList.Count)
                return null;

            return stackList[position];
        }

        /// <summary>
        /// Clears the stack of objects encountered.
        /// </summary>
        public void ClearStack()
        {
            stackMap.Clear();
            stackList.Clear();
        }

        public void AddIgnoreAttribute(Type type)
        {
            ignoredAttributeType = type;
        }

        public Type IgnoredAttributeType
        {
            get { return ignoredAttributeType; }
        }

        public IConverter RemoveConverter(Type type)
        {
            object converter = converterMap[type];
            converterMap[type] = null;
            return (IConverter) converter;
        }

        public virtual bool ContainsType(object o)
        {
            foreach (DictionaryEntry entry in stackMap)
                if (EntryKeyIs(entry, o)) return true;
            return false;
        }

        private static bool EntryKeyIs(DictionaryEntry entry, object o)
        {
            return entry.Key.GetType().Equals(o.GetType());
        }

        public virtual object GetOfType(object matchingObj)
        {
            foreach (DictionaryEntry entry in stackMap)
                if (EntryKeyIs(entry, matchingObj)) return entry.Key;
            return null;
        }
    }
}