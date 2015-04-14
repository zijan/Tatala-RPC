using System;
using System.Reflection;
using System.Xml;

namespace Xstream.Core.Converters
{
	/// <summary>
	/// Converts a pointer (System.Reflection.Pointer) to xml and back.
	/// </summary>
	/// <remarks>
	/// Not implemented to convert pointer types.
	/// </remarks>
	internal class PointerConverter : IConverter
	{
		private static readonly Type __type = typeof( Pointer );

		/// <summary>
		/// Register is called by a MarshalContext to allow the
		/// converter instance to register itself in the context
		/// with all appropriate value types and interfaces.
		/// </summary>
		public void Register(IMarshalContext context)
		{
			context.RegisterConverter( __type, this );
		}

		/// <summary>
		/// Converts the object passed in to its XML representation.
		/// The XML string is written on the XmlTextWriter.
		/// </summary>
		public void ToXml(object value, FieldInfo field, XmlTextWriter xml, IMarshalContext context)
		{
		}

		/// <summary>
		/// Converts the XmlNode data passed in, back to an actual
		/// .NET instance object.
		/// </summary>
		/// <returns>Object created from the XML.</returns>
		public object FromXml(object parent, FieldInfo field, Type type, XmlNode xml, IMarshalContext context)
		{
			return null;
		}
	}
}
