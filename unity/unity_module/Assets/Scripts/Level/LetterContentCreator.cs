using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using UnityEngine;
using Util;
using Random = System.Random;

namespace Level
{

    public class LetterContentCreator
    {

        public List<string> GenerateLettersFromWords(Params parameters)
        {
            var permutations = CreatePermutations("", parameters.Words, parameters.MinLength, parameters.MaxLength);
            var random = new Random();

            return permutations
                .OrderBy(element => element.Length)
                .Last()
                .ToList()
                .Select(character => character.ToString())
                .ToList()
                .ShuffleWith(random);
        }

        private List<string> CreatePermutations(string combination, List<string> toCombineWith, int minOutputLength, int maxOutputLength)
        {
            var result = new List<string>();

            foreach (string word in toCombineWith)
            {
                var newList = toCombineWith
                    .Select(el => el)
                    .Where(el => el != word)
                    .ToList();

                var newCombinaction = combination + word;
                if (newCombinaction.Length < maxOutputLength)
                {
                    if (newCombinaction.Length > minOutputLength)
                    {
                        result.Add(newCombinaction);
                    }
                    result.AddRange(CreatePermutations(newCombinaction, newList, minOutputLength, maxOutputLength));
                }
                else
                {
                    var toAdd = newCombinaction.Length == maxOutputLength ? newCombinaction : combination;
                    result.Add(toAdd);
                    break;
                }
            }

            return result;
        }

        public class Params
        {
            public List<string> Words { get; set; }

            public int MinLength { get; set; } = 0;
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

}