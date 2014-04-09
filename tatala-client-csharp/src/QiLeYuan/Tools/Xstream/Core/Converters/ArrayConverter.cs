using System;
using System.Reflection;
using System.Xml;

namespace Xstream.Core.Converters
{
	/// <summary>
	/// Converts an array (System.Array) of objects to XML and back.
	/// </summary>
	internal class ArrayConverter : IConverter
	{
		private static readonly Type __type = typeof( Array );

		/// <summary>
		/// Register is called by a MarshalContext to allow the
		/// converter instance to register itself in the context
		/// with all appropriate value types and interfaces.
		/// </summary>
		public void Register(IMarshalContext context)
		{
			context.RegisterConverter( __type, this );
			context.Alias( "array", __type );
		}

		/// <summary>
		/// Converts the object passed in to its XML representation.
		/// The XML string is written on the XmlTextWriter.
		/// </summary>
		public void ToXml(object value, FieldInfo field, XmlTextWriter xml, IMarshalContext context)
		{
			Array array				= value as Array;
			Type arrayType			= array.GetType();

			context.WriteStartTag( arrayType, field, xml );
			
			int stackIx	= context.GetStackIndex( value );

			if ( stackIx >= 0 )
				xml.WriteAttributeString( "ref", stackIx.ToString() );
			else
			{
				context.Stack( array );

				foreach ( object child in array )
				{
					IConverter converter;

					if ( child != null )
						converter = context.GetConverter( child.GetType() );
					else
						converter = context.GetConverter( null );

                    if (converter == null) throw new ConversionException("Couldnot find converter for: " + child.GetType() + " having value: " + child);
					converter.ToXml( child, null, xml, context );
				}
			}

			context.WriteEndTag( arrayType, field, xml );
		}

		/// <summary>
		/// Converts the XmlNode data passed in, back to an actual
		/// .NET instance object.
		/// </summary>
		/// <returns>Object created from the XML.</returns>
		public object FromXml( object parent, FieldInfo field, Type type, XmlNode xml, IMarshalContext context )
		{
			Array array;

			if ( xml.Attributes[ "ref" ] != null )
			{
				int stackIx		= int.Parse( xml.Attributes[ "ref" ].Value );
				array			= context.GetStackObject( stackIx ) as Array;
			}
			else
			{
				int childCount	= xml.ChildNodes.Count;
				array			= Activator.CreateInstance( type, new object[] { childCount } ) as Array;

                // Add the object to the stack
                context.Stack(array);
				int i			= 0;
				foreach ( XmlNode child in xml.ChildNodes )
				{
					Type memberType			= null;
					IConverter converter	= context.GetConverter( child, ref memberType );
				
					array.SetValue( converter.FromXml( null, null, memberType, child, context ), i );

					i++;
				}
			}
			
			return array;
		}
	}
}