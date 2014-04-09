using System;
using System.Runtime.Serialization;

namespace Xstream.Core
{
    /// <summary>
    /// Exception that occurs then (de)serialization of an object fails.
    /// </summary>
    [Serializable]
    public class ConversionException : Exception
    {
        internal ConversionException(SerializationInfo info, StreamingContext context) : base(info, context) {}
        internal ConversionException(string message) : base(message) {}
        internal ConversionException(string message, Exception innerException) : base(message, innerException) {}
    }
}