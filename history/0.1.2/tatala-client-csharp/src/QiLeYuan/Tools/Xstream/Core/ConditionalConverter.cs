using System;

namespace Xstream.Core
{
    public interface ConditionalConverter : IConverter
    {
        bool Match(Type type);
    }
}