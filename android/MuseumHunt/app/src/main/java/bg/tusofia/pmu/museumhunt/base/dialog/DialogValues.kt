package bg.tusofia.pmu.museumhunt.base.dialog

data class DialogValues(var title: String?,
                        var message: String?,
                        var positiveBtnTxt: String?,
                        var positiveBtnCallback: (() -> Unit)?,
                        var negativeBtnText: String?,
                        var negativeBtnCallback: (() -> Unit)?,
                        var modal: Boolean = false) {

    constructor() : this(null, null, null, null, null, null)
}