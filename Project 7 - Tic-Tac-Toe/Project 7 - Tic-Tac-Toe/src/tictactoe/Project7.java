/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import javax.swing.UIManager;

/**
 * Project 7 - Tic-Tac-Toe
 * @author Rio Weber
 * @version 1.0
 */
public class Project7
{
    public static void main(String[] args)
    {
        try
        {
            // Make Frame look like Windows
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e)
        {
            System.out.println("Error: Something happened.");
        }
        new TicTacToe();
    }
}