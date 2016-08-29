for i in "jboss" "db-derby" "hadoop" "hibernate" "ArgoUML" "checkstyle" "cayenne" "geotools" "jena" "Vuze" "Compiere" "axion" "c-jdbc" "colt" "columba"  "drawswf" "fit-java" "freecs" "heritrix" "iReport" ",informa" "ivatagroupware" "jFin" "jOggPlayer" "jasml" "jext" "jgraphpad" "jmoney" "jparse" 
do 
	printf "$i,"
	cat $1 | grep $i | wc -l	
done
