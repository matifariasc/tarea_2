/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package herenciap;

import java.util.Scanner;


/**
 *
 * @author mfarias
 */
public class Persona {
    public 
        String nombre,rut,dir,telf;
        int edad;
        Scanner leer = new Scanner(System.in);
        Persona (){
            this.nombre="";
            this.rut="";
            this.dir="";
            this.telf="";
            this.edad=0;

        }
        void pedirDatos(){
            System.out.print("Escriba Nombre :");
            this.nombre= leer.next();
            System.out.print("Escribir el Rut :");
            this.rut = leer.next();
            System.out.print("Escribir la Dirrecion :");
            this.dir= leer.next();
            System.out.print("Escribir el Telefono :");
            this.telf = leer.next();
            System.out.print("Escriba la Edad:");
            this.edad = leer.nextInt();
        
            
        }
        void mostrarDatos(){
            System.out.print("Nombre:"+ this.nombre);
            System.out.print("Rut:"+ this.rut);
            System.out.print("Direccion:"+ this.dir);
            System.out.print("Telefono:"+ this.telf);
            System.out.print("Edad"+ this.edad);
            
        }
    
}
