package com.apolis.conditional_stm

import java.util.*
import java.util.regex.Pattern
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

fun main(){
    var sys=URSystem()

}
fun checkInput(pattern:String):String {
    val regex= Regex(pattern)
    val sc = Scanner(System.`in`)
    val task=sc.next()
    if (regex.matches(task)) {
        return task
    } else {
        println("invalid input, please re-enter:")
        return checkInput(pattern)
    }
}
class URSystem {
    init {
        mainMenu()
    }
    fun mainMenu(){
        println("*********************************************")
        println("**********University Result System***********")
        println("*********************************************\n")
        println("1.Enroll a new Student")
        println("2.Open Student menu")
        println("3.Create a new Course")
        println("4.Create a new Subject")
        println("5.All Courses List")
        println("6.All Subject List")
        println("7.Show all student")
        println()
        println("0.EXIT")

        println("please select an operation from the list:")
        when(checkInput("[0-7]")){
            "0"->{
                return
            }
            "1"->{
                enrollStudent()
            }
            "2"->{
                if(Student.allStudent.size<1){
                    println("Student Menu is not available until there are students enrolled in the system")
                }
                else
                    studentMenu(selecetStudent())
            }
            "3"->{
                createCourse()
            }
            "4"->{
                createSubject()
            }
            "5"->{
                for(i in 0 until Course.allCourse.size){
                    println("$i .-> ${Course.allCourse[i].name}")
                }
            }
            "6"->{
                println(Subject.allSubject.toString())
            }
            "7"-> {
                studentSortMenu()
            }
        }
        mainMenu()
    }
    fun studentSortMenu(){
        println("============Student Menu==================")
        println("1.Display Students in ascending alphabet order")
        println("2.Display Students in descending alphabet order")
        println("3.Display Students in ascending percentage order")
        println("4.Display Students in descending percentage order")
        println()
        println("0.Back to main menu")
        println("please select an operation from the list:")
        when(checkInput("[0-4]")){
            "0"->{
                return
            }
            "1"->{
                Student.allStudent.sortWith(cpName())
            }
            "2"->{
                Student.allStudent.sortWith(cpNameD())
            }
            "3"->{
                Student.allStudent.sortWith(cpPercentage())
            }
            "4"-> {
                Student.allStudent.sortWith(cpPercentageD())
            }
        }
        for(i in 0 until Student.allStudent.size) {
            println("$i .-> ${Student.allStudent[i].name}    ${Student.allStudent[i].percentage}%")
        }
    }
    fun selecetStudent():Student{
        println("==========================================")
        val sc = Scanner(System.`in`)
        for(i in 0 until Student.allStudent.size){
            println("$i . -> ${Student.allStudent[i].name}")
        }
        println("please select a student by entering the index:")
        val studentIndex=checkInput("[0-${Student.allStudent.size-1}]").toInt()
        return Student.allStudent[studentIndex]
    }
    fun studentMenu(stu: Student){
        println("============Student Info==================")
        println("Name: ${stu.name}    Birthday: ${stu.DOB}")
        println("Percentage: ${stu.percentage}%    Mobile: ${stu.mobile}")
        println("Address: ${stu.address} ")
        println("============Student Menu==================")
        println("1.Grade this Student")
        println("2.Check all Courses this Student is taking")
        println("3.Check all Subject this Student is taking")
        println("4.Check if he/she have Passed ALL Course he/she is taking")
        println("5.Enroll to a course")
        println()
        println("0.Back to main menu")
        println("please select an operation from the list:")
        when(checkInput("[0-5]")){
            "0"->{
                return
            }
            "1"->{
                gradeStudent(stu)
            }
            "2"->{
                for(i in 0 until stu.enrolled.size) {
                    println("$i .-> ${stu.enrolled[i].name} ")
                }
            }
            "3"->{
                println(stu.subjectAndGrades.toString())
            }
            "4"->{
                stu.isPassAll()
            }
            "5"->{
                stu.enroll()
            }
        }
        studentMenu(stu)
    }
    fun enrollStudent(){
        println("-----Start to Enroll Student----")
        val sc = Scanner(System.`in`)
        println("Please Enter the name of the Student")
        val sName=sc.nextLine()
        println("Please Enter the mobile number of $sName")
        val phone=sc.nextLong()
        println("Please Enter the date of birth of $sName")
        println("(in format mm/dd/yyyy)")
        val sDOB= checkInput("[0-1][0-9]/[0-3][0-9]/[1-2][0-9][0-9][0-9]")
        println("Please Enter the address of $sName")
        sc.skip("\\R")
        val sa=sc.nextLine()

        var student=Student(sName,phone,sDOB,sa)
        println("----Successfully enrolled student $sName----")
    }
    fun gradeStudent(s:Student){
        println("-----Start to Grade Student ${s.name}----")
        val sc = Scanner(System.`in`)
        for(i in s.subjectAndGrades){
            println("Please Enter the grade of subject ${i.key} for the Student ${s.name}")
            val grade=sc.nextInt()
            s.subjectAndGrades.put(i.key,grade)
        }
        s.computePercentage()
        println("-----Grading Completed------")
    }
    fun createSubject(){
        println("-----Start to create Subject----")
        val sc = Scanner(System.`in`)
        println("Please Enter the name of the Subject")
        val sName=sc.next()
        println("Please Enter the max mark for the Subject $sName")
        val sMM=sc.nextInt()
        println("Please Enter the minimum passing mark for the Subject $sName")
        val sPM=sc.nextInt()
        var sbj=Subject(sName,sMM,sPM)
        println("----Successfully created Subject $sName----")
    }
    fun createCourse(){
        println("-----Start to create Course----")
        val sc = Scanner(System.`in`)
        println("Please Enter the name of the Course")
        val cName=sc.next()
        println("Please Enter the amount of subjects are needed for the Course $cName")
        val cN=sc.nextInt()
        if(cN>Subject.allSubject.size){
            println("not enough subject to create this course")
            return
        }
        var course=Course(cName,cN)
        println("----Successfully created Course $cName----")
    }

}
class Course(val name: String,val n:Int){
    val subjects: Array<Subject?>
    init {
        subjects=Array<Subject?>(n,{null})
        chooseSubjects()
        allCourse.add(this)
    }
    private fun chooseSubjects(){
        val sc= Scanner(System.`in`)
        for(i in 0 until Subject.allSubject.size){
            println("$i .-> ${Subject.allSubject[i]}")
        }
        println("please select $n subject for this course:")
        println("(input the index number) ")
        for(i in 0 until n){
            subjects[i]=Subject.allSubject[checkInput("[0-${Subject.allSubject.size-1}]").toInt()]
        }
    }
    companion object{
        var allCourse=ArrayList<Course>()
    }
}
data class Subject(val name:String,val mark:Int,val pm:Int){
    companion object{
        var allSubject=ArrayList<Subject>()
    }
    init {
        allSubject.add(this)
    }
}

class Student(val name: String,val mobile:Long, val DOB:String ,val address: String){
    companion object{
        var allStudent=ArrayList<Student>()
    }
    val bday:Int
    val bmonth:Int
    val byear:Int
    var percentage:Double
    lateinit var enrolled:ArrayList<Course>
    lateinit var subjectAndGrades:HashMap<Subject,Int>
    init {
        val dob=DOB.split("/")
        bday=dob[1].toInt()
        bmonth=dob[0].toInt()
        byear=dob[2].toInt()
        percentage=0.0
        enrolled= ArrayList<Course>()
        subjectAndGrades= HashMap<Subject,Int>()
        allStudent.add(this)
    }
    fun computePercentage(){
        var sum=0
        var max=0
        for(i in subjectAndGrades.entries){
            sum += i.value
            max += i.key.mark
        }
        percentage=(sum.toDouble()/max)*100
    }
    fun enroll(){
        val sc= Scanner(System.`in`)
        for(i in 0 until Course.allCourse.size){
            println("$i .-> ${Course.allCourse[i].name}")
        }
        println("please select a course to enroll:")
        println("(input the index number) ")
        val c= Course.allCourse[checkInput("[0-${Course.allCourse.size-1}]").toInt()]
        enrolled.add(c)
        for(s in c.subjects){
            if (s != null) {
                subjectAndGrades.put(s,0)
            }
        }
    }
    fun isPassCourse(c:Course):Boolean{
        for(i in c.subjects){
            if (i != null) {
                if(subjectAndGrades.get(i)!! < i.pm){
                    println("Student $name have not passed course ${c.name}")
                    return false
                }
            }
        }
        println("Student $name have passed course ${c.name}")
        return true
    }
    fun isPassAll():Boolean{
        for(c in enrolled){
            if(!isPassCourse(c)){
                println("Student $name have not yet passed course")
                return false;
            }
        }
        println("Student $name have passed all course!!")
        return true;
    }
}
class cpName : Comparator<Student>{
    override fun compare(p0: Student?, p1: Student?): Int {
        if (p0 !=null && p1 != null) {
            return p0.name.compareTo(p1.name)
        }
        return 0
    }
}
class cpNameD : Comparator<Student>{
    override fun compare(p0: Student?, p1: Student?): Int {
        if (p0 !=null && p1 != null) {
            return p1.name.compareTo(p0.name)
        }
        return 0
    }
}
class cpPercentage : Comparator<Student>{
    override fun compare(p0: Student?, p1: Student?): Int {
        if (p0 !=null && p1 != null) {
            if(p0.percentage-p1.percentage>0)
                return 1
            else if (p0.percentage-p1.percentage<0)
                return -1
            else return 0
        }
        return 0
    }
}
class cpPercentageD : Comparator<Student>{
    override fun compare(p0: Student?, p1: Student?): Int {
        if (p0 !=null && p1 != null) {
            if(p0.percentage-p1.percentage>0)
                return -1
            else if (p0.percentage-p1.percentage<0)
                return 1
            else return 0
        }
        return 0
    }
}

