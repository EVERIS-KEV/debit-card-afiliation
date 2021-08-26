package com.everis.debitcardafiliation.constant;

public enum Constants {
	;
	public static class Path {

		public static final String GATEWAY = "44.196.6.42:8090/";
		public static final String SERVICE_PATH = "service";
		public static final String HTTP_CONSTANT = "http://";

		public static final String CUSTOMERS_PATH = HTTP_CONSTANT.concat(GATEWAY).concat(SERVICE_PATH)
				.concat("/customers");
		public static final String LOGIC_PATH = HTTP_CONSTANT.concat(GATEWAY).concat(SERVICE_PATH).concat("/logic");
		public static final String CREDIT_ACCOUNT_PATH = HTTP_CONSTANT.concat(GATEWAY).concat(SERVICE_PATH)
				.concat("/credits");
		public static final String CURRENT_ACCOUNT_PATH = HTTP_CONSTANT.concat(GATEWAY).concat(SERVICE_PATH)
				.concat("/currentAccount");
		public static final String SAVING_ACCOUNT_PATH = HTTP_CONSTANT.concat(GATEWAY).concat(SERVICE_PATH)
				.concat("/savingAccount");
		public static final String FIXED_ACCOUNT_PATH = HTTP_CONSTANT.concat(GATEWAY).concat(SERVICE_PATH)
				.concat("/fixedTermAccount");
	}

	public static class PathService {

		public static final String VERIFY_NUMBER_ACCOUNT = "/verifyByNumberAccount/";
		public static final String VERIFY_CUSTOMER = "/verifyId/{id}";
		public static final String NUMBER_ACCOUNT = "/byNumberAccount/{number}";
	}

	public static class Messages {

		public static final String INCORRECT_DATA = "Datos incorrectos.";
		public static final String AFILIATE_NOT_ACCOUNT = "Esta cuenta no está afiliada a su tarjeta.";
		public static final String CLIENT_NOT_FOUND = "Cliente no econtrado.";
		public static final String CLIENT_SUCCESS = "Registrado correctamente.";
		public static final String INVALID_DATA = "Datos inválidos.";

		public static final String UPDATE_TARGET = "Tarjeta actualizada.";
		public static final String NOT_UPDATE_TARGET = "Esta cuenta ya está afiliada a su tarjeta.";
		public static final String WARNING_TARGET = "Esta es su única cuenta y debe ser la principal.";
		public static final String WARNING_TARGET_PRINCIPAL = "Ya tiene una cuenta principal.";
		public static final String TARGET_NOT_FOUND = "Tarjeta no encontrada.";

		public static final String SUCCESS_PROCESS_ACCOUNT = "Cuenta añadida.";
		public static final String LIMIT_EXCEED_ACCOUNT = "El monto sobre pasa el todas sus cuentas.";
		public static final String LIMIT_EXCEED_ACCOUNT_PRINCIPAL = "El monto sobre pasa el de la cuenta principal, operaciones realizadas.";

		public static final String SUCCESS_OPERATION = "Operación realizada.";
	}
}
