package com.zubiri.concesionario;
/**
 * @author Xabi A., Koldo, Nerea
 */

import java.sql.*;

public class ConexionDB {

	private Connection conn;
	private Statement st;
	private CallableStatement cst;

	public Statement getStatement() {
		return st;
	}

	public void setStatement(Statement st) {
		this.st = st;
	}
	
	/**
	 * Hace una conexi�n a la base de datos "vehiculosdb" con el usuario admin_vehiculos
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */

	public void connect() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String jdbcUrl = "jdbc:mysql://10.18.124.125:3306/vehiculosdb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
		conn = DriverManager.getConnection(jdbcUrl, "admin_vehiculos", "1234");
		st = conn.createStatement();
	}
	
	/**
	 * Hace la conexi�n con la base de datos
	 * @param ip
	 * @param database Base de datos a utilizar
	 * @param username Nombre del usario
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */

	public void connect(String ip, String database, String username, String password)
			throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/" + database + "?user=" + username
				+ "&password=" + password + "&serverTimezone=UTC&useSSL=false");
		setStatement(conn.createStatement());
	}

	
	/**
	 * Metodo de cierre para la conexi�n.
	 * @throws SQLException
	 */
	public void close() throws SQLException {
		conn.close();
	}
	
	/**
	 * M�todo para mostrar todos los datos de la tabla vehiculos guardados en la BD
	 * @return los datos existentes en cada linea
	 * @throws SQLException
	 */

	

	public ResultSet getVehiculos() throws SQLException {
		cst = conn.prepareCall("call getVehiculos()");
		return cst.executeQuery();

	}
	
	/**
	 * M�todo para mostrar los datos de la tabla coches guardados en la BD
	 * @return los datos existentes en cada linea
	 * @throws SQLException
	 */

	public ResultSet getCoches() throws SQLException {
		cst = conn.prepareCall("call getCoches()");  
		return cst.executeQuery();
	}
	/**
	 * M�todo para mostrar los datos de la tabla camiones guardados en la BD
	 * @return los datos existentes en cada linea
	 * @throws SQLException
	 */

	public ResultSet getCamiones() throws SQLException {

		cst = conn.prepareCall("call getCamiones()");  
		return cst.executeQuery();


	}
	
	/**
	 * 
	 * @param numBastidor el n�mero de bastidor del veh�culo que queremos llamar
	 * @return los datos solicitados del coche
	 * @throws SQLException
	 * @throws Exception
	 */
	public Coche getCoche(String numBastidor) throws SQLException, Exception {
		Coche coche = new Coche();
		cst = conn.prepareCall("call getCoche('"+numBastidor+"')");
		ResultSet rs = cst.executeQuery();
		rs.next();
		coche.setMatricula(rs.getString("matricula").toUpperCase());
		coche.setNumBastidor(rs.getString("numBastidor").toUpperCase());
		coche.setColor(rs.getString("color"));
		coche.setNumAsientos(rs.getInt("numAsientos"));
		coche.setPrecio(rs.getFloat("precio"));
		
		Serie serie = new Serie();
		serie.setNumSerie(rs.getInt("numSerie"));
		serie.setFechaFabricacion(rs.getInt("fechaFabricacion"));
		serie.setMarca(rs.getString("marca"));
		serie.setModelo(rs.getString("modelo"));
		coche.setSerie(serie);
		
		coche.setNumPuertas(rs.getInt("numPuertas"));
		coche.setCapacidadMaletero(rs.getFloat("capacidadMaletero"));
		return coche;
	}
	
	/**
	 * 
	 * @param numBastidor el n�mero de bastidor del veh�culo que queremos llamar
	 * @return los datos solicitados del camion
	 * @throws SQLException
	 * @throws Exception
	 */
	public Camion getCamion(String numBastidor) throws SQLException, Exception {
		Camion camion = new Camion();
		cst = conn.prepareCall("call getCamion('"+numBastidor+"')");
		ResultSet rs = cst.executeQuery();
		rs.next();
		camion.setMatricula(rs.getString("matricula").toUpperCase());
		camion.setNumBastidor(rs.getString("numBastidor").toUpperCase());
		camion.setColor(rs.getString("color"));
		camion.setNumAsientos(rs.getInt("numAsientos"));
		camion.setPrecio(rs.getFloat("precio"));
		
		Serie serie = new Serie();
		serie.setNumSerie(rs.getInt("numSerie"));
		serie.setFechaFabricacion(rs.getInt("fechaFabricacion"));
		serie.setMarca(rs.getString("marca"));
		serie.setModelo(rs.getString("modelo"));
		camion.setSerie(serie);
		
		camion.setCarga(rs.getFloat("carga"));
		camion.setTipoMercancia(rs.getString("tipoMercancia").charAt(0));
		return camion;
	}
	
	/**
	 * Inserta en la base de datos los datos de un nuevo coche
	 * @param Objeto coche con los datos
	 * @throws SQLException
	 */

	public void insertarCoche(Coche coche) throws SQLException {

		cst = conn.prepareCall("call insertarCoche('" + coche.getMatricula() + "','" + coche.getNumBastidor() + "','"
				+ coche.getColor() + "'," + coche.getNumAsientos() + "," + coche.getPrecio() + ",'" + coche.getSerie().getNumSerie()
				+ "',' 'disponible'," + coche.getNumPuertas() + "," + coche.getCapacidadMaletero() + ");");

		cst = conn.prepareCall("call insertarCoche('" + coche.getMatricula().toUpperCase() + "','" + coche.getNumBastidor() + "','"
				+ coche.getColor() + "'," + coche.getNumAsientos() + "," + coche.getPrecio() + "," + coche.getSerie().getNumSerie()
				+ ",'disponible','coche'," + coche.getNumPuertas() + "," + coche.getCapacidadMaletero() + ");");

		cst.execute();
	}
	
	/**
	 * Inserta en la base de datos los datos de un nuevo camion
	 * @param Objeto camion con los datos
	 * @throws SQLException
	 */

	public void insertarCamion(Camion camion) throws SQLException {
		cst = conn.prepareCall("call insertarCamion(" + camion.getMatricula().toUpperCase() + "','" + camion.getNumBastidor() + "','"
				+ camion.getColor() + "'," + camion.getNumAsientos() + "," + camion.getPrecio() + ",'"
				+ camion.getSerie().getNumSerie() + "','disponible'," + camion.getCarga() + ",'"
				+ camion.getTipoMercancia() + "');");
		cst.execute();
	}
	
	/**
	 * M�todo para modificar datos del coche
	 * @param numBastidor como clave para saber que coche modificar
	 * @param Objeto coche con los datos
	 * @throws SQLException
	 */

	public void modificarCoche(String numBastidor, Coche coche) throws SQLException {
		st.executeUpdate("update vehiculos set matricula='" + coche.getMatricula() + "', numBastidor='"
				+ coche.getNumBastidor() + "', color='" + coche.getColor() + "', numAsientos=" + coche.getNumAsientos()
				+ ", precio=" + coche.getPrecio() + ", serie=" + coche.getSerie().getNumSerie() + 
				"where numBastidor='" + numBastidor + "';");
		st.executeUpdate("update coches set numPuertas=" + coche.getNumPuertas() + 
						 ", capacidadMaletero=" + coche.getCapacidadMaletero()+" where numBastidor='"+numBastidor+"';");
	}
	
	/**
	 * M�todo para modificar datos del cami�n
	 * @param numBastidor como clave para saber qu� cami�n modificar
	 * @param Objeto cami�n con los datos
	 * @throws SQLException
	 */

	public void modificarCamion(String numBastidor, Camion camion) throws SQLException {
		st.executeUpdate("update camiones set matricula='" + camion.getMatricula() + "', numBastidor='"
				+ camion.getNumBastidor() + "', color='" + camion.getColor() + "', numAsigentos="
				+ camion.getNumAsientos() + ", precio=" + camion.getPrecio() + ", serie='" + camion.getSerie()
				+ "', estado='" + camion.getEstado() + "', carga=" + camion.getCarga() + ", tipoMercancia='"
				+ camion.getTipoMercancia() + "' where numBastidor='" + numBastidor + "';");
		st.executeUpdate("update from camiones set carga='"+ camion.getCarga()+ "', tipoMercancia='" 
				+ camion.getTipoMercancia() + "' where numBastidor='" + numBastidor + "'");
	}
	
	/**
	 * M�todo para eliminar (vender) el veh�culo de nuestra BD
	 * @param numBastidor como clave para saber qu� veh�culos vamos a "vender"
	 * @throws SQLException
	 */
	public void eliminarVehiculo(String numBastidor) throws SQLException {
		cst = conn.prepareCall("call eliminarVehiculo('"+numBastidor+"');");
		cst.execute();
	}
	
	
	/**
	 * Devuelve el objecto serie dado su n�mero de serie
	 * @param numSerie int con el n�mero de serie
	 * @return Objeto Serie
	 * @throws SQLException
	 */
	public Serie getSerie(Integer numSerie) throws SQLException {
		ResultSet rs = st.executeQuery("select * from series where numserie="+numSerie+";");
		Serie serie = new Serie();
		rs.next();
		serie.setNumSerie(rs.getInt("numSerie"));
		serie.setFechaFabricacion(rs.getInt("fechaFabricacion"));
		serie.setMarca(rs.getString("marca"));
		serie.setModelo(rs.getString("modelo"));
		return serie;
	}

}
