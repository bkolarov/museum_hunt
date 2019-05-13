using System.Collections.Generic;
using Random = System.Random;
using System.Linq;
using System.Text;
using UnityEngine;

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

        public static void Print<T>(this List<T> list)
        {
            Debug.Log($"[{string.Join(", ", list)}]");
        }

        public static void Print<T>(this T[,] cells)
        {
            var stringBuilder = new StringBuilder();
            for (int y = 0; y < cells.GetLength(1); y++)
            {
                var l = new List<T>();
                for (int x = 0; x < cells.GetLength(0); x++)
                {
                    l.Add(cells[x, y]);
                }
                stringBuilder.AppendLine(string.Join(", ", l));
            }

            Debug.Log(stringBuilder.ToString());
        }

        public static void ForeachIndexed<T>(this T[,] array, ApplyIndexedFunc<T> apply)
        {
            for (int x = 0; x < array.GetLength(0); x++)
            {
                for (int y = 0; y < array.GetLength(1); y++)
                {
                    apply(x, y, array[x, y]);
                }
            }
        }

        public static ISet<T> ToSet<T>(this IEnumerable<T> enumerable)
        {
            return new HashSet<T>(enumerable);
        }

        public static IEnumerable<T> Apply<T>(this IEnumerable<T> enumerable, ApplyFunc<T> apply)
        {
            List<T> list = enumerable.ToList();

            list.ForEach(element => apply(element));

            return list;
        }

        public delegate void ApplyIndexedFunc<T>(int x, int y, T element);

        public delegate void ApplyFunc<T>(T element);

    }

}