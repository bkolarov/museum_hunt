using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace Level.Repository
{
    public class LevelData
    {

        public List<string> HintWords { get; }

        public LevelData(List<string> hintWords)
        {
            HintWords = hintWords;
        }
    }
}