package com.example.basedatosejemplo.datos;

import java.io.Serializable;
import java.util.Date;

import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

public class Persona implements Parcelable, Serializable {
	private static final long serialVersionUID = -7278327517900717947L;

	private String nombre;
	private String cedula;
	private Date fechaNac;
	private String estadoCivil;
	private Boolean discapacitado;
	private Float estatura;

	public Persona() {
		super();
	}

	public Persona(Parcel in) {
		nombre = in.readString();
		cedula = in.readString();
		fechaNac = new Date(in.readLong());
		estadoCivil = in.readString();
		discapacitado = (in.readInt() == 1);
		estatura = in.readFloat();

	}

	public Persona(String nombre, String cedula, Date fechaNac,
			String estadoCivil, Boolean discapacitado, Float estatura) {
		super();
		this.nombre = nombre;
		this.cedula = cedula;
		this.fechaNac = fechaNac;
		this.estadoCivil = estadoCivil;
		this.discapacitado = discapacitado;
		this.estatura = estatura;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public Date getFechaNac() {
		return fechaNac;
	}

	public void setFechaNac(Date fechaNac) {
		this.fechaNac = fechaNac;
	}

	public String getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public Boolean getDiscapacitado() {
		return discapacitado;
	}

	public void setDiscapacitado(Boolean discapacitado) {
		this.discapacitado = discapacitado;
	}

	public Float getEstatura() {
		return estatura;
	}

	public void setEstatura(Float estatura) {
		this.estatura = estatura;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(nombre);
		dest.writeString(cedula);
		dest.writeLong(fechaNac.getTime());
		dest.writeString(estadoCivil);
		dest.writeInt(discapacitado ? 1 : 0);
		dest.writeFloat(estatura);
	}

	public static final Parcelable.Creator<Persona> CREATOR = new Parcelable.Creator<Persona>() {
		public Persona createFromParcel(Parcel in) {
			return new Persona(in);
		}

		public Persona[] newArray(int size) {
			return new Persona[size];
		}
	};

	@SuppressLint("NewApi")
	public String toString(String type) {
		String resultado = "";
		
		if ((type != null) && !type.isEmpty()) {
			if (type.toLowerCase().equals("json"))
				resultado = new GsonBuilder().create().toJson(this);
			if (type.toLowerCase().equals("xml")) {
				XStream xs = new XStream();
				xs.alias("persona", Persona.class);
				resultado =  xs.toXML(this); 
			}
		}
		else 
			resultado = this.toString();
		
		return resultado;
	};

}
