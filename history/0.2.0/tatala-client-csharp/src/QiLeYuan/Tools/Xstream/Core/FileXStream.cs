using System;
using System.IO;

namespace Xstream.Core
{
    public class FileXStream : IXStream
    {
        // Fields
        private readonly string fileName;
        private readonly XStream xStream;

        // Methods
        public FileXStream(string fileName)
        {
            this.fileName = fileName;
            xStream = new XStream();
        }

        public void AddConverter(IConverter converter)
        {
            xStream.AddConverter(converter);
        }

        public void AddIgnoreAttribute(Type ignoredAttributeType)
        {
            xStream.AddIgnoreAttribute(ignoredAttributeType);
        }

        public object FromFile()
        {
            return xStream.FromXml<object>(File.ReadAllText(fileName));
        }

        public object FromXml(string xml)
        {
            return xStream.FromXml<object>(xml);
        }

        public string ToXml(object value)
        {
            string xml = xStream.ToXml(value);
            File.WriteAllText(fileName, xml);
            return xml;
        }

        public XStream XStream
        {
            get { return xStream; }
        }
    }
}