#!/bin/bash
# Muestra la tabla directamente desde MySQL (útil para demostrar que los cambios quedan en la BD)
mysql -upat -ppat123 pat_db -e "SELECT * FROM usuario;" 2>/dev/null
