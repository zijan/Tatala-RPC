using System;
using System.Reflection;
using QiLeYuan.Tatala.socket.to;
using QiLeYuan.Tools.debug;

/**
 * This class is local target, simply provider local-method-call.
 * 
 * @author JimT
 *
 */
namespace QiLeYuan.Tatala.executor {

    public class LocalTarget : ServerTarget {
        public Object execute(TransferObject to) {

            String calleeClass = to.getCalleeClass();
            String calleeMethod = to.getCalleeMethod();

            Object retobj = null;
            try {
                Type type = Type.GetType(calleeClass);
                Object instance = Activator.CreateInstance(type);
                MethodInfo method = type.GetMethod(calleeMethod);
                retobj = method.Invoke(instance, new object[] { to });
            } catch (Exception e) {
                Logging.LogError(e.ToString());
            }

            return retobj;
        }
    }
}