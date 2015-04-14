using System;
using System.Collections.Generic;
using System.Reflection;

namespace Xstream.Core
{
    /// <summary>
    /// Understands id of fields of entities. Limitation that Field names should be same
    /// </summary>
    internal class IdFields
    {
        private Dictionary<Type, FieldInfo> fields = new Dictionary<Type, FieldInfo>(3);
        private string fieldName;
        
        public void AddField(Type holdingObjectType, FieldInfo fieldInfo)
        {
            if (fieldName == null) fieldName = fieldInfo.Name;
            else if (!fieldName.Equals(fieldInfo.Name)) throw new NotSupportedException("Fields cannot have different names");

            fields.Add(holdingObjectType, fieldInfo);
        }

        public object GetFieldValue(object holderObject)
        {
            if (holderObject == null) throw new NullReferenceException();

            foreach (KeyValuePair<Type, FieldInfo> keyValuePair in fields)
            {
                if (keyValuePair.Key.IsAssignableFrom(holderObject.GetType())) return keyValuePair.Value.GetValue(holderObject);
            }
            throw new NotImplementedException(holderObject.GetType() + " has no \"id\" fields in it or is not supported");
        }

        private Type GetBaseType(Type type)
        {
            foreach (KeyValuePair<Type, FieldInfo> keyValuePair in fields)
            {
                if (keyValuePair.Key.IsAssignableFrom(type)) return keyValuePair.Key;
            }
            throw new NotImplementedException(type + " has no \"id\" fields in it or is not supported");
        }

        public Type FieldType(Type holderObjectType)
        {
            return GetField(holderObjectType).FieldType;
        }

        public bool HasIdFieldNamed(string fieldName)
        {
            return this.fieldName.Equals(fieldName);
        }

        public FieldInfo GetField(Type holderObjectType)
        {
            return fields[GetBaseType(holderObjectType)];
        }
    }
}