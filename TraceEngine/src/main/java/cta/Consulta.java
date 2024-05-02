package cta;

import java.io.Serializable;
import java.util.Vector;

import cta.designe.listener.ModelFilter;

//Una consulta es un vector de modulos + filtro y su nombre de consulta
public class Consulta implements Serializable {
	String sistemaConsulta;
	String nameConsulta;
	Vector<Modulo> modulosActivos;
	String filtro;// filtro de texto rapido del tipo "cadena&cadenaotra|otracadena&cotraadenaotra"
	Vector<ModelFilter> filtrosActivos;
	//String numeroMaquina;



	public Consulta(String sistemaConsulta, String nameConsulta, Vector<Modulo> modulosActivos, String filtro,
			Vector<ModelFilter> filtrosActivos) {
		super();
		this.sistemaConsulta = sistemaConsulta;
		this.nameConsulta = nameConsulta;
		this.modulosActivos = modulosActivos;
		this.filtro = filtro;
		this.filtrosActivos = filtrosActivos;
		
	}
	

	public Consulta() {
		super();
	}

	public Vector<ModelFilter> getFiltrosActivos() {
		return filtrosActivos;
	}

	public void setFiltrosActivos(Vector<ModelFilter> filtrosActivos) {
		this.filtrosActivos = filtrosActivos;
	}

	public String getSistemaConsulta() {
		return sistemaConsulta;
	}

	public void setSistemaConsulta(String sistemaConsulta) {
		this.sistemaConsulta = sistemaConsulta;
	}

	public String getNameConsulta() {
		return nameConsulta;
	}

	public void setNameConsulta(String nameConsulta) {
		this.nameConsulta = nameConsulta;
	}

	public String getFiltro() {
		return filtro;
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}

	public Vector<Modulo> getModulosActivos() {
		return modulosActivos;
	}

	public void setModulosActivos(Vector<Modulo> modulos) {
		this.modulosActivos = modulos;
	}

	public void insertarModuloActivo(Modulo mod) {
		if (!(this.getModulosActivos().contains(mod)))
			this.getModulosActivos().add(mod);
	}

	public String toString() {
		return this.getNombreConsultaFull();
	}

	public String getNombreConsultaFull() {
		return this.getSistemaConsulta()+":"+this.getNameConsulta();
	}
	
	
}
