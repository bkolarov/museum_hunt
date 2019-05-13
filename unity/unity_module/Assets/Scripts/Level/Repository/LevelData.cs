using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace Level.Repository
{
    [Serializable]
    public class LevelData
    {
        public List<string> HintWords;

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