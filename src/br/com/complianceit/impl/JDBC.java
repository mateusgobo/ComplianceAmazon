/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.complianceit.impl;

import br.com.complianceit.api.IConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mateusgobo
 */
public class JDBC implements IConnection{
    
    private Connection sqlConnection;
    
    public Connection openConnection(String url, String user, String pass){
        try {
            Class.forName(DRIVER);
            sqlConnection = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JDBC.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex){
            Logger.getLogger(JDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sqlConnection;
    }
    
    public boolean closeConnection(PreparedStatement pstmt, ResultSet rs) throws SQLException {
        try{
            if(rs != null){
                rs.close();
            }
            if(pstmt != null){
               pstmt.close();
            }
            if(this.sqlConnection != null){
                this.sqlConnection.close();
            }
            return true;
        }catch(SQLException e){
            throw new SQLException("Erro ao encerrar jdbc connection...");
        }
    }

    public Connection getConnection() {
        return sqlConnection;
    }
}
