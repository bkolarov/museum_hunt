using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Events;

namespace Level
{
    public class FinishLineWatcher : MonoBehaviour
    {
        public FinishLinePassListener OnPass = new FinishLinePassListener();

        protected void OnTriggerEnter2D(Collider2D collision)
        {
            OnPass.Invoke();
        }

        [System.Serializable]
        public class FinishLinePassListener : UnityEvent { }
    }
}

