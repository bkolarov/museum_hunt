﻿using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace Level.Repository
{
    public class LevelDataRepository : MonoBehaviour
    {
        private LevelData LevelData;

        // Start is called before the first frame update
        void Start()
        {
            var words = new List<string>
            {
                "Pesho",
                "Ivancho",
                "Mariika",
                "Kircho",
                "Tisho"
            };
            LevelData = new LevelData(words);
        }

        public LevelData GetLevelData()
        {
            return LevelData;
        }
    }
}