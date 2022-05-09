package code.Android.Studio.MyCalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()
{
    private var canAddOperation = false                   // Bien kiem tra co the them dau toan hoc khong
    private var canAddNumber = true                       // Bien kiem tra co the them so duoc khong
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun numberAction(view: View)      // ham them ki tu so 
    {
        if(view is Button)    // neu che do xem la button 
        {
            if(view.text == ".")
            {
                if(canAddNumber)
                    workingsTV.append(view.text)   // them ki tu tu bat ki nut nao
                canAddNumber = false               // de bien nay ve false the hien rang khong the them ki tu laf so nua
            }
            else
                workingsTV.append(view.text)
            canAddOperation = true                // co the them ki tu la ki tu toan hoc
        }
    }
    fun operationAction(view: View)    // ham them ki tu toan hoc
    {
        if(view is Button && canAddOperation)     // neu view la button va dang co the them ki tu toan hoc
        {
            workingsTV.append(view.text)         // them ki tu toan hoc vao string workingstv
            canAddOperation = false              // sau khi them ki tu toan hoc thi chuyen canAddOperation de khong cho them ki tu toan hoc nua
            canAddNumber = true                  // chuyen thanh true de co the them ki tu so
        }
    }
    
    /* 
    Ham de xoa tat ca phep tinh va ket qua bang cach cai dat chuoi workingtextview va resulttv bang null
    */
    fun allClearAction(view: View)
    {
        workingsTV.text = ""
        resultsTV.text = ""
    }
    
    fun backSpaceAction(view: View)
    {
        val length = workingsTV.length()    // do dai chuoi working tv
        if(length > 0)
            workingsTV.text = workingsTV.text.subSequence(0, length - 1) // xoa 1 ki tu cuoi cung
    }
    
     private fun digitsOperators(): MutableList<Any>    // ham chuyen cac ki tu so va toan hoc vào mảng list
    {
        val list = mutableListOf<Any>()    // tao bien danh sach 
        var currentDigit = ""            // chuoi so hien tai
        for(character in workingsTV.text)  // ki tu o trong chuoi workingsTV
        {
            if(character.isDigit() || character == '.') // neu ki tu la chuoi so hoac la ki tu thap phan
                currentDigit += character               // them ki tu hien tai vao chuoi currentDigit  
            else                                        // neu khong phai 
            {
                list.add(currentDigit.toFloat())       // them chuoi currentDigit chuyen sang kieu float vao list
                currentDigit = ""                      // chuyen bien current ve null
                list.add(character)                    // them ki tu toan hoc vao list
            }
        }
        if(currentDigit != "")                         // de them nhung ki tu con lai vao list
            list.add(currentDigit.toFloat())
        return list
    }
    
    fun equalsAction(view: View)    // ham tinh ket qua
    {
        resultsTV.text = calculateResults()
    }
    
    private fun calculateResults(): String
    {
        val digitsOperators = digitsOperators()
        if(digitsOperators.isEmpty())     // neu string digitsO ma trong thi tra ve null
            return ""

        val timesDivision = timesDivisionCalculate(digitsOperators)   // tinh phep tinh nhan, chia
        if(timesDivision.isEmpty())
            return ""

        val result = addSubtractCalculate(timesDivision)        // tinh phep tinh cong tru
        return result.toString()
    }
    
     private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any>
    {
        var list = passedList
        while (list.contains('x') || list.contains('/'))  // neu trong passlist ma co ki nhan hoac chia thi them vao 
        {
            list = calcTimesDiv(list)                    // thuc hien phep tinh nhan, chia tra ve list
        }
        return list
    }
     private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any>   // ham thuc hien phep tinh nhan. chia
    {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size  
        for(i in passedList.indices)      // vong for nhay vi tri hop le cua list
        {
            if(passedList[i] is Char && i != passedList.lastIndex && i < restartIndex) // neu phan tu passedlist la 1 ki tu va i chua pai la phan tu cuoi cung va < rsindex
            {
                val operator = passedList[i]                    
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                when(operator)
                {
                    'x' ->
                    {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' ->
                    {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else ->                                // neu khong phai phep tinh nhan chia thi de nguyen va tra ve list
                    {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }
            if(i > restartIndex)
                newList.add(passedList[i])
        }
        return newList
    }
    
    private fun addSubtractCalculate(passedList: MutableList<Any>): Float   // ham thuc hien phep tinh cong, tru
    {
        var result = passedList[0] as Float

        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex)
            {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+')
                    result += nextDigit
                if (operator == '-')
                    result -= nextDigit
            }
        }
        return result
    }
   
}



















