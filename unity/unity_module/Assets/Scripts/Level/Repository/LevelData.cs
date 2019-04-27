using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace Level.Repository
{
    [Serializable]
    public class LevelData
    {
        // "{ "HintWords": ["Pesho", "Kircho", "Mariika", "Tisho", "Cecka"] }"
        public List<string> HintWords { get; }

        public LevelData(List<string> hintWords)
        {
            HintWords = hintWords;
        }
    }
}

namespace Level
{
    [Serializable]
    public class ResultData
    {
        public string[] FoundWords;

        public ResultData(string[] foundWords)
        {
            FoundWords = foundWords;
        }
    }
}