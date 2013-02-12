/*
 * This file is part of Port@l
 * Port@l 3.0 - Portal Engine and Management System
 * Copyright (C) 2010  Isotrol, SA.  http://www.isotrol.com
 *
 * Port@l is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Port@l is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Port@l.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * extras.js
 * 
 * Contains extra native Javascript functions to be used from the entry point.
 */

/*
 * Añade eventos a los elementos dinamicamente.
 * Copiado de funcionesauxiliares.js, en Sherp@
 * 
 * Ejemplo uso desde GWT:
 			$wnd.addEvent(
				$wnd.buscadorNotRel,
				"load",
				function() {
					$wnd.buscadorNotRel.insertar = function(id) {
						//this es el objeto sobre el que se añade el evento
						//e.d. $wnd.buscadorNotRel
						//this.opener.asociarNoticia(id);
						@com.isotrol.encuestas.client.GestorEncuestasApp::asociarNoticia(Ljava/lang/String;)(id);
					};
				},
				true,	
				null
			);
 */
function addEvent(el, evNombre, func, useCapture, objeto) 
{
	if (el.addEventListener){
		el.addEventListener(evNombre,function fL(e){
			if(typeof func == 'function')
				func();
			else
				eval(func);
		},useCapture);
		return true;
	} else if (el.attachEvent){
		var r = el.attachEvent("on"+evNombre, function fL(e){
			if(typeof func == 'function')
				func();
			else
				eval(func);
		});
		return r;
	} else {
		alert("No se ha podido capturar el evento.");
	}
}
