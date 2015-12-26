
#ifndef _ASOC_SERVER_H_
#define _ASOC_SERVER_H_

#include "asoc.h"

typedef struct NodoLista NodoLista;

struct NodoLista{
	int id;
	NodoDiccionario* diccionario;
	NodoLista* sig;
};

NodoDiccionario* crearNodoDiccionario(char * clave, char* valor);

NodoLista * crearNodoLista(int id, char * clave, char* valor);

#endif
