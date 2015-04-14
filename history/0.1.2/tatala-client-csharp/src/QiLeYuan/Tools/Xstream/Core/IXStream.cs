using System;

namespace Xstream.Core
{
    public interface IXStream
    {
        void AddConverter(IConverter converter);
        void AddIgnoreAttribute(Type ignoredAttributeType);
        object FromXml(string xml);
        string ToXml(object value);
    }
}