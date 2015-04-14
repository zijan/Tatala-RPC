using System;
using System.Collections;
using System.Reflection;
using System.Reflection.Emit;

namespace Xstream.Core
{
    /// <summary>
    /// The DynamicInstanceBuilder creates new Types in dynamic 
    /// assemblies, based on existing types.
    /// </summary>
    internal class DynamicInstanceBuilder
    {
        public const string __typePrefix = ".xsdyn~";

        private static ModuleBuilder moduleBuilder = null;
        private static Hashtable typeMap = Hashtable.Synchronized(new Hashtable());

        private static ModuleBuilder GetModuleBuilder()
        {
            if (moduleBuilder == null)
            {
                AssemblyName assemblyName = new AssemblyName();
                assemblyName.Name = ".xstreamDynamic";
                AppDomain domain = AppDomain.CurrentDomain;
                AssemblyBuilder ab = domain.DefineDynamicAssembly(assemblyName, AssemblyBuilderAccess.Run);
                moduleBuilder = ab.DefineDynamicModule(".xstreamDynamic.dll");
            }

            return moduleBuilder;
        }

        /// <summary>
        /// Generates a dynamic Type inheriting the specified type, adding a 
        /// default constructor for instance generation.
        /// </summary>
        public static object GetDynamicInstance(Type type)
        {
            if (typeof (MulticastDelegate).IsAssignableFrom(type))
            {
                DynamicMethod method = new DynamicMethod("XStreamDynamicDelegate", typeof(void), GetDelegateParameterTypes(type), typeof(object));
                ILGenerator generator = method.GetILGenerator();
                generator.Emit(OpCodes.Ret);
                return method.CreateDelegate(type);
            }
            if (type.IsSealed)
                throw new ConversionException("Impossible to construct type: " + type.ToString());

            // Check if we already have the type defined
            string typeName = __typePrefix + type.ToString().Replace("+", "\\+");

            lock (typeMap)
            {
                Type dynamicType = typeMap[typeName] as Type;

                if (dynamicType == null)
                {
                    ModuleBuilder moduleBuilder = GetModuleBuilder();
                    TypeBuilder typeBuilder = moduleBuilder.DefineType(typeName,
                                                                       TypeAttributes.Class | TypeAttributes.NotPublic,
                                                                       type);

                    ConstructorBuilder cb =
                        typeBuilder.DefineConstructor(MethodAttributes.Private, CallingConventions.Standard, new Type[0]);
                    cb.GetILGenerator().Emit(OpCodes.Ret);

                    dynamicType = typeBuilder.CreateType();
                    typeMap[typeName] = dynamicType;
                }

                return Activator.CreateInstance(dynamicType, true);
            }
        }

        private static Type[] GetDelegateParameterTypes(Type d)
        {
            if (d.BaseType != typeof (MulticastDelegate))
                throw new ApplicationException("Not a delegate.");

            MethodInfo invoke = d.GetMethod("Invoke");
            if (invoke == null)
                throw new ApplicationException("Not a delegate.");

            ParameterInfo[] parameters = invoke.GetParameters();
            Type[] typeParameters = new Type[parameters.Length];
            for (int i = 0; i < parameters.Length; i++)
            {
                typeParameters[i] = parameters[i].ParameterType;
            }
            return typeParameters;
        }
    }
}