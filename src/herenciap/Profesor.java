/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package herenciap;

/**
 *
 * @author mfarias
 */
public class Profesor extends Persona {
    private
        String categoria;
        float nota1,nota2,nota3,nota4;
    public
        String ramo;
    Profesor(){
        this.ramo="";
        this.categoria="";
        this.nota1=0;
        this.nota2=0;
        this.nota3=0;
        this.nota4=0; 
    }
    void pedirDatos(){
        super.pedirDatos();
        System.out.print("Escribir el Ramo:");
        this.ramo= leer.next();
        System.out.print("Escribir el Categoria:");
        this.categoria= leer.next(); 
    }
    public float getNota1() {return nota1;}
    public void setNota1(float nota1) {this.nota1 = nota1;}
    public float getNota2() {return nota2;}
    public void setNota2(float nota2) {this.nota2 = nota2;}
    public float getNota3() {return nota3;}
    public void setNota3(float nota3) {this.nota3 = nota3;}
    public float getNota4() {return nota4;}
    public void setNota4(float nota4) {this.nota4 = nota4;}
    
    void pedirNotas(){
        System.out.print("Escribir la Nota1 :");
        setNota1(leer.nextFloat());
        System.out.print("Escribir la Nota2 :");
        setNota2(leer.nextFloat());
        System.out.print("Escribir la Nota3 :");
        setNota3(leer.nextFloat());
        System.out.print("Escribir la Nota4 :");
        setNota4(leer.nextFloat());
    }
    float promedioNotas(){
        return (getNota1()+getNota2()+getNota3()+getNota4())/4;
    }
}
