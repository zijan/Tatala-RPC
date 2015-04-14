using System;
using System.Collections.Generic;
using System.Reflection;
using System.Xml;

namespace Xstream.Core.Converters
{
    /// <summary>
    /// Converts an array (System.Array) of objects to XML and back.
    /// </summary>
    internal class GenericListConverter : IConverter
    {
        private IConverter converter = new ArrayConverter();
        private static readonly Type __type = typeof(List<>);

        /// <summary>
        /// Register is called by a MarshalContext to allow the
        /// converter instance to register itself in the context
        /// with all appropriate value types and interfaces.
        /// </summary>
        public void Register(IMarshalContext context)
        {
            context.RegisterConverter(__type, this);
            context.Alias("list", __type);
        }

        /// <summary>
        /// Converts the object passed in to its XML representation.
        /// The XML string is written on the XmlTextWriter.
        /// </summary>
        public void ToXml(object value, FieldInfo field, XmlTextWriter xml, IMarshalContext context)
        {
            MethodInfo method = value.GetType().GetMethod("ToArray");
            object invoke = method.Invoke(value, new object[] {});
            converter.ToXml(invoke, field, xml, context);
        }

        /// <summary>
        /// Converts the XmlNode data passed in, back to an actual
        /// .NET instance object.
        /// </summary>
        /// <returns>Object created from the XML.</returns>
        public object FromXml(object parent, FieldInfo field, Type type, XmlNode xml, IMarshalContext context)
        {
			object array;

			if ( xml.Attributes[ "ref" ] != null )
			{
				int stackIx		= int.Parse( xml.Attributes[ "ref" ].Value );
				array			= context.GetStackObject( stackIx ) as Array;
			}
			else
			{
			    array = Activator.CreateInstance(type);

                // Add the object to the stack
                context.Stack(array);
				foreach ( XmlNode child in xml.ChildNodes )
				{
					Type memberType			= null;
					IConverter conv	= context.GetConverter( child, ref memberType );									
                    object item = conv.FromXml( null, null, memberType, child, context );
				    MethodInfo method = array.GetType().GetMethod("Add");
				    method.Invoke(array, new object[] {item});
				}
			}
            return array;
            //return converter.FromXml(parent, field, type, xml, context);            
        }
    }
}