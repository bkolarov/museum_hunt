using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using UnityEngine;
using Random = System.Random;

namespace Level
{

    public class LetterContentCreator
    {

        public List<string> GenerateLettersFromWords(Params parameters)
        {
            var permutations = CreatePermutations("", parameters.Words, parameters.MaxLength);
            var random = new Random();

            List<string> result = permutations
                .Select(permutation => permutation)
                .Where(permutation => permutation.Length >= parameters.MinLength)
                .ToList()
                .RandomElement(random)
                .ToList()
                .Select(character => character.ToString())
                .ToList()
                .Shuffle(random);

            return result;
        }

        private List<string> CreatePermutations(string combination, List<string> toCombineWith, int maxOutputLength)
        {
            var result = new List<string>();

            foreach (string word in toCombineWith)
            {
                var newList = toCombineWith
                    .Select(el => el)
                    .Where(el => el != word)
                    .ToList();

                if (combination.Length + word.Length < maxOutputLength)
                {
                    result.AddRange(CreatePermutations(combination + word, newList, maxOutputLength));
                }
                else
                {
                    var toAdd = combination;
                    if (combination.Length + word.Length == maxOutputLength)
                    {
                        toAdd += word;
                    }
                    result.Add(toAdd);
                    break;
                }
            }

            return result;
        }

        public class Params
        {
            public List<string> Words { get; set; }

            public int MinLength { get; set; }
            public int MaxLength { get; set; }

            public Params(List<string> words, int minLength, int maxLength)
            {
                Words = words;
                MinLength = minLength;
                MaxLength = maxLength;
            }

            public Params(List<string> words)
            {
                Words = words;
            }
        }
    }

    internal static class ListExtension
    {
        public static string RandomElement(this List<string> list, Random random)
        {
            return list[random.Next(list.Count)];
        }

        public static List<string> Shuffle(this List<string> list, Random random)
        {
            int n = list.Count;
            while (n > 1)
            {
                n--;
                int k = random.Next(n + 1);
                string value = list[k];
                list[k] = list[n];
                list[n] = value;
            }

            return list;
        }
    }

}