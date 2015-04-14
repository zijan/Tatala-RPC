using System;
using System.IO;
using System.Text;
using System.Xml;

namespace Xstream.Core
{
	/// <summary>
	/// Class used to marshal and unmarshal instance objects
	/// using the Xstream XML.
	/// </summary>
	internal class XStreamMarshaller
	{
		private readonly string __rootElement = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>";
	    /// <summary>
		/// Converts the given object to XML representation,
		/// using the specific MarshalContext for serialization.
		/// </summary>
        public string ToXml( object value, IMarshalContext context )
		{
			try
			{
				Type objectType			= value.GetType();
				IConverter converter	= context.GetConverter( objectType );

				StringBuilder sbuf		= new StringBuilder();
			
				using ( StringWriter sw = new StringWriter( sbuf ) )
				{
					XmlTextWriter writer = new XmlTextWriter( sw );
					converter.ToXml( value, null, writer, context );
					writer.Close();
				}
			 
				return sbuf.ToString();
			}
			catch ( ConversionException )
			{
				throw;
			}
			catch ( Exception e )
			{
				throw new ConversionException( e.Message, e );
			}
			finally
			{
				context.ClearStack();
			}
		}

		/// <summary>
		/// Converts the xml string parameter back to a class instance,
		/// using the specified context for type mapping.
		/// </summary>
		public object FromXml( string xml, IMarshalContext context )
		{
			try
			{
				XmlDocument xmlDoc		= new XmlDocument();

                if (!xml.StartsWith(__rootElement))
                    xml = __rootElement + Environment.NewLine + xml;
			    
				xmlDoc.LoadXml( xml );

                xmlDoc.RemoveChild(xmlDoc.FirstChild);
			    
				Type type				= null;
				IConverter converter	= context.GetConverter( xmlDoc.FirstChild, ref type );

				return converter.FromXml( null, null, type, xmlDoc.FirstChild, context );
			}	
			catch ( ConversionException )
			{
				throw;
			}
			catch ( Exception e )
			{
				throw new ConversionException( e.Message, e );
			}
			finally
			{
				context.ClearStack();
			}
		}
	}
}
