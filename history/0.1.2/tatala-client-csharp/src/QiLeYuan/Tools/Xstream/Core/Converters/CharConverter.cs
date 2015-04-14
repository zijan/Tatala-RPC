using System;
using System.Reflection;
using System.Xml;

namespace Xstream.Core.Converters
{
	/// <summary>
	/// Converts a char (System.Char) to xml and back.
	/// </summary>
	public class CharConverter : IConverter
	{
		private static readonly Type __type			= typeof( char );
		private static readonly Type __arrayType	= typeof( char[] );

		/// <summary>
		/// Register is called by a MarshalContext to allow the
		/// converter instance to register itself in the context
		/// with all appropriate value types and interfaces.
		/// </summary>
		public void Register(IMarshalContext context)
		{
			context.RegisterConverter( __type, this );
			context.RegisterConverter( __arrayType, this );
			context.Alias( "char", __type );
			context.Alias( "chars", __arrayType );
		}

		/// <summary>
		/// Converts the object passed in to its XML representation.
		/// The XML string is written on the XmlTextWriter.
		/// </summary>
		public void ToXml(object value, FieldInfo field, XmlTextWriter xml, IMarshalContext context)
		{
			Type type = value.GetType();

			if ( value is char[] )
			{
				char[] buffer = value as char[];

				context.WriteStartTag( __arrayType, field, xml );
				xml.WriteChars( buffer, 0, buffer.Length );
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
		/// Converts the XmlNode data passed in, back to an actual
		/// .NET instance object.
		/// </summary>
		/// <returns>Object created from the XML.</returns>
		public object FromXml(object parent, FieldInfo field, Type type, XmlNode xml, IMarshalContext context)
		{
			if ( type == __arrayType )
				return xml.InnerText.ToCharArray();
			else
				return char.Parse( xml.InnerText );
		}
	}
}
