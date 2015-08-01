<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>MathML on the cloud</title>
</head>
<body>
	<div align="center">
		<h1>Processing MathML with Java</h1>
		<br />
		<table border="1" align="center">
			<tbody>
				<tr>
					<th>Supported operations and functions</th>
					<th>Content Markup Files</th>
				</tr>
				<tr>
					<td>
						<table>
							<tr align="center">
								<th>Operations</th>
								<th>Functions</th>
							</tr>
							<tr align="center">
								<td>Plus</td>
								<td>Sin</td>
							</tr>
							<tr align="center">
								<td>Minus</td>
								<td>Cos</td>
							</tr>
							<tr align="center">
								<td>Division</td>
								<td>Tan</td>
							</tr>
							<tr align="center">
								<td>Multiplication</td>
								<td>Sqrt</td>
							</tr>
							<tr align="center">
								<td>-</td>
								<td>Power</td>
							</tr>
							<tr align="center">
								<td>-</td>
								<td>Cosh</td>
							</tr>
							<tr align="center">
								<td>-</td>
								<td>Abs</td>
							</tr>
							<tr align="center">
								<td>-</td>
								<td>ACos</td>
							</tr>
							<tr align="center">
								<td>-</td>
								<td>ASin</td>
							</tr>
							<tr align="center">
								<td>-</td>
								<td>ATan</td>
							</tr>
							<tr align="center">
								<td>-</td>
								<td>Ceil</td>
							</tr>
							<tr align="center">
								<td>-</td>
								<td>Tanh</td>
							</tr>
						</table>
					</td>
					<td>
						<table align="center">
							<tr>
								<td><a href="mathMLRepository/mathFile_1.math">mathFile_1</a></td>
							</tr>
							<tr>
								<td><a href="mathMLRepository/mathFile_2.math">mathFile_2</a></td>
							</tr>
							<tr>
								<td><a href="mathMLRepository/mathFile_3.math">mathFile_3</a></td>
							</tr>
							<tr>
								<td><a href="mathMLRepository/mathFile_4.math">mathFile_4</a></td>
							</tr>
							<tr>
								<td><a href="mathMLRepository/mathFile_9.math">mathFile_9</a></td>
							</tr>
							<tr>
								<td><a href="mathMLRepository/mathFile_10.math">mathFile_10</a></td>
							</tr>
							<tr>
								<td><a href="mathMLRepository/mathFile_11.math">mathFile_11</a></td>
							</tr>
							<tr>
								<td><a href="mathMLRepository/mathFile_12.math">mathFile_12</a></td>
							</tr>
							<tr>
								<td><a href="mathMLRepository/mathFile_13.math">mathFile_13</a></td>
							</tr>
							<tr>
								<td><a href="mathMLRepository/mathFile_14.math">mathFile_14</a></td>
							</tr>
							<tr>
								<td><a href="mathMLRepository/mathFile_15.math">mathFile_15</a></td>
							</tr>
							<tr>
								<td><a href="mathMLRepository/mathFile_16.math">mathFile_16</a></td>
							</tr>
							<tr>
								<td><a href="mathMLRepository/mathFile_20.math">mathFile_20</a></td>
							</tr>
						</table>
					</td>
				</tr>
			</tbody>
		</table>

		<br /> <br />

		<table border="1">
			<tbody>
				<tr>
					<th>Calculator Form</th>
					<th>Validation Form</th>
				</tr>

				<tr align="center">
					<td>
						<form name="filesForm" action="/mathml.web/calculate"
							method="post" enctype="multipart/form-data">
							Content Markup File <input type="file" name="Content Markup File">
							<br /> <br /> <input type="submit" name="Submit"
								value="Calculate">
						</form>
					</td>

					<td>
						<form name="filesForm" action="/mathml.web/validate" method="post"
							enctype="multipart/form-data">
							MathML File <input type="file" name="MathML File"> <br />
							<br /> <input type="submit" name="Submit" value="Validate">
						</form>
					</td>
				</tr>
				<tr>
					<th colspan="2">Transform Form - <i>Under development</i></th>
				</tr>
				<tr align="center">
					<td colspan="2">
						<form name="filesForm" action="/mathml.web/transform"
							method="post" enctype="multipart/form-data">
							Presentation Markup File <input type="file"
								name="Presentation Markup File"> <br /> <br /> <input
								type="submit" name="Submit" value="Transform">
						</form>
					</td>
				</tr>

			</tbody>
		</table>

	</div>
</body>
</html>