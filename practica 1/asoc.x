/* Archivo asoc.x: Protocolo de un servicio básico de asociación */

typedef NodoDiccionario* NodoDiccionarioPTR;

struct NodoDiccionario{
	string clave<>;
	string valor<>;
	NodoDiccionarioPTR sig;
};

enum Estado{
	OK,
	Sustituido,
	NoEncontrado,
	FalloMemoria
};

union ResultadoBusqueda switch(Estado s){
	case OK: string valor<>;
	default: void;
};

union ResultadoEnumerar switch(Estado s){
	case OK: NodoDiccionarioPTR diccionario;
	default: void;
};

program ASOCIACIONPROG{
	version ASOCIACIONVERS {
		Estado PONERASOCIACION (int, string, string) = 1;
		ResultadoBusqueda OBTENERASOCIACION (int, string) = 2;
		Estado BORRARASOCIACION (int, string) = 3;
		ResultadoEnumerar ENUMERAR (int) = 4;
	} = 1;
} = 0x20000001;
