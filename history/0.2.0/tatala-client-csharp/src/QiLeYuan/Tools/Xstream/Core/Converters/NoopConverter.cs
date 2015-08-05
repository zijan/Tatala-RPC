using System;
using System.Reflection;
using System.Xml;

namespace Xstream.Core.Converters
{
    public class NoopConverter : IConverter
    {
        private static NoopConverter instance = new NoopConverter();
        private NoopConverter() {}

        public static IConverter Instance
        {
            get { return instance; }
        }

        public void Register(IMarshalContext context)
        {
        }

        public void ToXml(object value, FieldInfo field, XmlTextWriter xml, IMarshalContext context)
        {
        }

        public object FromXml(object parent, FieldInfo field, Type type, XmlNode xml, IMarshalContext context)
        {
            throw new NotImplementedException();
        }
    }
}