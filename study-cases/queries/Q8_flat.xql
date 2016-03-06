(: Os codigos e as areas das quadras dos bairros vizinhos ao bairro gutierrez
   Spatial Operator: Area :)

<blocks>
	LET $doc := document("city.xml")
	FOR $a in $doc//neighborhood,
			$b in $doc//neighborhood,
			$block in $doc//block
	WHERE $a/name = "gutierrez" AND Touches($a//gml:Polygon, $b//gml:Polygon) = 1 AND $b/containsNB/@ref = $block/code
	RETURN
	<block>
		<code>{$block/code/text()}</code>
		<area>{Area($block/gml:Polygon)}</area>
	</block>
</blocks>