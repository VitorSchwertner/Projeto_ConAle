package com.conAle.projeto.Model;

import com.conAle.projeto.controller.DataBaseException;
import com.conAle.projeto.util.DataBaseConnectionManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class UsuarioDAO {

    DataBaseConnectionManager db;

    public void AcessarBanco() {
        try {
            db = new DataBaseConnectionManager(DataBaseConnectionManager.MYSQL, "conale", "conale", "conale_local_dev");
            db.connectDataBase();
        } catch (DataBaseException ex) {
            ex.printStackTrace();
        }
    }

    public void FecharBanco() {
        try {
            if (db != null) {
                db.closeConnection();
            }
        } catch (DataBaseException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void PreencherTabela(DefaultTableModel modelo) {
        AcessarBanco();
        try {
            ResultSet rs = db.runQuerySQL("SELECT * FROM usuario ORDER BY id;");
            modelo.setRowCount(0);
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("email")
                });
            }
            rs.close();
        } catch (SQLException | DataBaseException ex) {
            System.out.println(ex.getMessage());
        } finally {
            FecharBanco();
        }
    }

    public Usuario buscarPorId(int id) {
        AcessarBanco();
        Usuario usuario = null;
        try {
            ResultSet rs = db.runPreparedQuerySQL(
                    "SELECT * FROM usuario WHERE id = ?;", id
            );
            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenhaHash(rs.getString("senhaHash"));
            }
            rs.close();
        } catch (SQLException | DataBaseException ex) {
            System.out.println(ex.getMessage());
        } finally {
            FecharBanco();
        }
        return usuario;
    }

    public Usuario buscarPorEmail(String email) {
        AcessarBanco();
        Usuario usuario = null;
        try {
            ResultSet rs = db.runPreparedQuerySQL(
                    "SELECT * FROM usuario WHERE email = ?;", email
            );
            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenhaHash(rs.getString("senhaHash"));
            }
            rs.close();
        } catch (SQLException | DataBaseException ex) {
            System.out.println(ex.getMessage());
        } finally {
            FecharBanco();
        }
        return usuario;
    }

    public int ProximoId() {
        AcessarBanco();
        int id = 0;
        try {
            ResultSet rs = db.runQuerySQL("SELECT MAX(id) AS id FROM usuario;");
            if (rs.next()) {
                id = rs.getInt("id") + 1;
            }
            rs.close();
        } catch (SQLException | DataBaseException e) {
            System.out.println("Erro ao buscar próximo ID: " + e.getMessage());
        } finally {
            FecharBanco();
        }
        return id;
    }

    public void Salvar(Usuario usuario, int editar) {
        AcessarBanco();
        try {
            if (editar == 0) {
                db.runPreparedSQL(
                        "INSERT INTO usuario (nome, email, senhaHash) VALUES (?, ?, ?);",
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getSenhaHash()
                );
            } else {
                Atualizar(usuario);
            }
        } catch (DataBaseException ex) {
            System.out.println(ex.getMessage());
        } finally {
            FecharBanco();
        }
    }

    public void Atualizar(Usuario usuario) throws DataBaseException {
        AcessarBanco();
        try {
            db.runPreparedSQL(
                    "UPDATE usuario SET nome = ?, email = ?, senhaHash = ? WHERE id = ?;",
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getSenhaHash(),
                    usuario.getId()
            );
        } finally {
            FecharBanco();
        }
    }

    public void Excluir(Usuario usuario) {
        AcessarBanco();
        try {
            db.runPreparedSQL("DELETE FROM usuario WHERE id = ?;",
                    usuario.getId()
            );
        } catch (DataBaseException ex) {
            System.out.println("Erro ao excluir usuário: " + ex.getMessage());
        } finally {
            FecharBanco();
        }
    }

    public ArrayList<Usuario> listarTodos() throws DataBaseException {
        AcessarBanco();
        ArrayList<Usuario> usuarios = new ArrayList<>();
        try {
            ResultSet rs = db.runQuerySQL("SELECT * FROM usuario ORDER BY id ASC;");
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenhaHash(rs.getString("senhaHash"));
                usuarios.add(usuario);
            }
            rs.close();
        } catch (SQLException ex) {
            throw new DataBaseException("Erro ao consultar usuários: " + ex.getMessage());
        } finally {
            FecharBanco();
        }
        return usuarios;
    }

    public boolean emailJaExiste(String email) {
        AcessarBanco();
        boolean existe = false;
        try {
            ResultSet rs = db.runPreparedQuerySQL(
                    "SELECT 1 FROM usuario WHERE email = ?;", email
            );
            existe = rs.next();
            rs.close();
        } catch (SQLException | DataBaseException ex) {
            System.out.println(ex.getMessage());
        } finally {
            FecharBanco();
        }
        return existe;
    }
}
