using System;
using System.Reflection;
using System.Xml;

namespace Xstream.Core.Converters
{
	/// <summary>
	/// Converts MethodInfo to XML and back.
	/// </summary>
	internal class MethodInfoConverter : IConverter
	{
		private static readonly Type __type = typeof( System.Reflection.MethodInfo );

		/// <summary>
		/// Register is called by a MarshalContext to allow the
		/// converter instance to register itself in the context
		/// with all appropriate value types and interfaces.
		/// </summary>
		public void Register(IMarshalContext context)
		{
			context.RegisterConverter( __type, this );
			context.Alias( "method", __type );
		}

		/// <summary>
		/// Converts the object passed in to its XML representation.
		/// The XML string is written on the XmlTextWriter.
		/// </summary>
		public void ToXml(object value, FieldInfo field, XmlTextWriter xml, IMarshalContext context)
		{
			MethodInfo info				= value as MethodInfo;
			ParameterInfo[] paramList	= info.GetParameters();

			if ( info.ReflectedType == null )
				throw new ConversionException( "Unable to serialize MethodInfo, no reflected type." );

            context.WriteStartTag( __type, field, xml );

			xml.WriteElementString( "base", context.GetTypeName( info.ReflectedType ) );
			xml.WriteElementString( "name", info.Name );
			
			xml.WriteStartElement( "params" );
			foreach ( ParameterInfo paramInfo in paramList )
				xml.WriteElementString( "param", context.GetTypeName( paramInfo.ParameterType ) );	
			xml.WriteEndElement();
			
			context.WriteEndTag( __type, field, xml );
		}

		/// <summary>
		/// Converts the XmlNode data passed in, back to an actual
		/// .NET instance object.
		/// </summary>
		/// <returns>Object created from the XML.</returns>
		public object FromXml(object parent, FieldInfo field, Type type, XmlNode xml, IMarshalContext context)
		{
			XmlNode node		= xml.SelectSingleNode( "base" );
			Type baseType		= Type.GetType( node.InnerText );

			// Get parameter types of the method
			node				= xml.SelectSingleNode( "params" );
			Type[] paramTypes	= new Type[ node.ChildNodes.Count ];
			int index			= 0;
			foreach ( XmlNode param in node.ChildNodes )
				paramTypes[ index++ ] = Type.GetType( param.InnerText );

			return baseType.GetMethod( xml.SelectSingleNode( "name" ).InnerText, paramTypes );
		}
	}
}
