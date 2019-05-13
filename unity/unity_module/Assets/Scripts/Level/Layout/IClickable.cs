using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace TilesLayout
{
    public delegate void GameObjectClickListener(GameObject gameObject);

    public interface IClickable
    {
        event GameObjectClickListener OnGameObjectClick;
    }
}