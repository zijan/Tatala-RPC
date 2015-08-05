using System;
using System.Reflection;
using System.Xml;

namespace Xstream.Core
{
    /// <summary>
    /// IConverter is the interface that all type converter instances
    /// should implement to get used with a MarshalContext.
    /// </summary>
    public interface IConverter
    {
        /// <summary>
        /// Register is called by a MarshalContext to allow the
        /// converter instance to register itself in the context
        /// with all appropriate value types and interfaces.
        /// </summary>
        void Register(IMarshalContext context);

        /// <summary>
        /// Converts the object passed in to its XML representation.
        /// The XML string is written on the XmlTextWriter.
        /// </summary>
        void ToXml(object value, FieldInfo field, XmlTextWriter xml, IMarshalContext context);

        /// <summary>
        /// Converts the XmlNode data passed in back to an actual
        /// .NET instance object.
        /// </summary>
        /// <returns>Object created from the XML.</returns>
        object FromXml(object parent, FieldInfo field, Type type, XmlNode xml, IMarshalContext context);
    }

    public interface AssignableConverter : IConverter
    {
        Type ConverteeType { get; }
    }
}