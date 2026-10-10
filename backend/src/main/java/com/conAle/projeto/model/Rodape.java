package com.conAle.projeto.Model;

public class Rodape
{
    private int id;
    private String telefone;
    private String instagram;
    private String email;
    private String diasSemana;
    private String horasTrabalhadas;
    private String endereco;

    public String getTelefone()
    {
        return telefone;
    }

    public void setTelefone( String telefone )
    {
        this.telefone = telefone;
    }

    public String getInstagram()
    {
        return instagram;
    }

    public void setInstagram( String instagram )
    {
        this.instagram = instagram;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail( String email )
    {
        this.email = email;
    }

    public String getDiasSemana()
    {
        return diasSemana;
    }

    public void setDiasSemana( String diasSemana )
    {
        this.diasSemana = diasSemana;
    }

    public String getHorasTrabalhadas()
    {
        return horasTrabalhadas;
    }

    public void setHorasTrabalhadas( String horasTrabalhadas )
    {
        this.horasTrabalhadas = horasTrabalhadas;
    }

    public String getEndereco()
    {
        return endereco;
    }

    public void setEndereco( String endereco )
    {
        this.endereco = endereco;
    }
}
