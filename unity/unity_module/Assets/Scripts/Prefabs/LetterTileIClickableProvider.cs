using System.Collections;
using System.Collections.Generic;
using System.ComponentModel;
using TilesLayout;
using UnityEngine;

namespace LetterTile
{
    public class LetterTileIClickableProvider : MonoBehaviour, IClickableProvider
    {
        public IClickable GetIClickable()
        {
            return gameObject.GetComponent(typeof(LetterTileBinding)) as LetterTileBinding;
        }
    }
}