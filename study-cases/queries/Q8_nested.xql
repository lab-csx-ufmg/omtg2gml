(: Os codigos e as areas das quadras dos bairros vizinhos ao bairro gutierrez
   Spatial Operator: Area :)

<blocks>
	LET $doc := document("city.xml")
	FOR $a in $doc//region/aggregationRN/neighborhood,
			$b in $doc//region/aggregationRN/neighborhood
	WHERE $a/name = "gutierrez" AND Touches($a//gml:Polygon, $b//gml:Polygon) = 1
	RETURN
	<block>
		<code>{$b/containsNB/block/code/text()}</code>
		<area>{Area($b/containsNB/block/gml:Polygon)}</area>
	</block>
</blocks>