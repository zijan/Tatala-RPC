using System;
using System.Reflection;
using System.Xml;

namespace Xstream.Core.Converters
{
	/// <summary>
	/// Converts a byte (System.Byte) to XML and back.
	/// </summary>
	public class ByteConverter : IConverter
	{
		private static readonly Type __type			= typeof( byte );
		private static readonly Type __arrayType	= typeof( byte[] );

		/// <summary>
		/// Register is called by a MarshalContext to allow the
		/// converter instance to register itself in the context
		/// with all appropriate value types and interfaces.
		/// </summary>
		public void Register(IMarshalContext context)
		{
			context.RegisterConverter( __type, this );
			context.RegisterConverter( __arrayType, this );
			context.Alias( "byte", __type );
			context.Alias( "bytes", __arrayType );
		}

		/// <summary>
		/// Converts the object passed in to its XML representation.
		/// The XML string is written on the XmlTextWriter.
		/// </summary>
		public void ToXml(object value, FieldInfo field, XmlTextWriter xml, IMarshalContext context)
		{
			Type type = value.GetType();

			if ( type.IsArray )
			{
				byte[] bytes = value as byte[];
				context.WriteStartTag( __arrayType, field, xml );
				xml.WriteBase64( bytes, 0, bytes.Length );
				context.WriteEndTag( __arrayType, field, xml );
			}
			else
			{
				context.WriteStartTag( __type, field, xml );
				xml.WriteString( value.ToString() );
				context.WriteEndTag( __type, field, xml );
			}
		}

		/// <summary>
		/// Converts the XmlNode data passed in back to an actual
		/// .NET instance object.
		/// </summary>
		/// <returns>Object created from the XML.</returns>
		public object FromXml(object parent, FieldInfo field, Type type, XmlNode xml, IMarshalContext context)
		{
			if ( type.IsArray )
				return Convert.FromBase64String( xml.InnerText );
			else
				return byte.Parse( xml.InnerText );
		}
	}
}
