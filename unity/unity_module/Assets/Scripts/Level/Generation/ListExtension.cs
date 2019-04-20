using System.Collections.Generic;
using Random = System.Random;

namespace Util
{
    public static class ListExtension
    {
        public static T RandomElement<T>(this List<T> list, Random random)
        {
            return list[random.Next(list.Count)];
        }

        public static List<T> ShuffleWith<T>(this List<T> list, Random random)
        {
            int n = list.Count;
            while (n > 1)
            {
                n--;
                int k = random.Next(n + 1);
                T value = list[k];
                list[k] = list[n];
                list[n] = value;
            }

            return list;
        }

        public static IEnumerable<T> Flatten<T>(this T[,] array2d)
        {
            foreach (T item in array2d)
            {
                yield return item;
            }
        }

        public static void ForeachIndexed<T>(this T[,] array, Apply<T> apply)
        {
            for (int x = 0; x < array.GetLength(0); x++)
            {
                for (int y = 0; y < array.GetLength(1); y++)
                {
                    apply(x, y, array[x, y]);
                }
            }
        }

        public delegate void Apply<T>(int x, int y, T element);
    }

}