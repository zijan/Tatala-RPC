using System.Reflection;

namespace Xstream.Core.Converters
{
    internal class TargetForceLoader
    {
        public static object GetTargetValue(object value) 
        {
            if (value == null) return value;
            ForceLoadTheEntityBecauseItIsLazy(value);

            FieldInfo interceptorField = value.GetType().GetField("__interceptor");
            if (interceptorField == null) return value;

            object interceptorValue = interceptorField.GetValue(value);
            FieldInfo targetField = interceptorValue.GetType().BaseType.GetField("_target", BindingFlags.NonPublic | BindingFlags.Public | BindingFlags.Instance);
            return targetField.GetValue(interceptorValue);
        }

        private static void ForceLoadTheEntityBecauseItIsLazy(object value)
        {
            try
            {
                value.GetType().GetMethod("get_Version", BindingFlags.Public | BindingFlags.Instance).Invoke(value, new object[] {});
            }
            catch (TargetInvocationException e)
            {
                throw e.InnerException.InnerException;
            }
        }
    }
}