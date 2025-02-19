Graphics 800,600,32,2
SetBuffer BackBuffer()

Global c#,ci#,ra#,zoom#,xoff#,yoff#
Global maxx%,maxy%,maxit%,bit1%

initvalues()
Repeat
	For i=0 To maxx-1
	
		If Not bit1% Then AppTitle "Julia-Fraktal   c = "+c#+" + "+ci#+"i     Iterationen = "+maxit%
		If bit1% Then AppTitle "Mandelbrot-Fraktal" + "          Iterationen = "+maxit%
		control()
		
		LockBuffer BackBuffer()
		
		For j=0 To maxy-1
			x#=2*Float(i-maxx/2)/(maxy*zoom) : y#=2*Float(j-maxy/2)/(maxy*zoom)
			x#=x#-xoff# : y#=y#-yoff#
			If Not bit1% Then col%=rgbfade(transform(x#,y#,c#,ci#,maxit%))
			If bit1% Then col%=rgbfade(transform(0,0,x#,y#,maxit%))
			WritePixelFast i,j,col%
		Next
		
		UnlockBuffer BackBuffer()
		Flip 0
	Next
	
Until KeyHit(1)
End

Function initvalues() ;Startwerte initialisieren
   ;c#=0 : ci#=0
	c#=-0.74 : ci#=0.12               ;MS
   ;c#=-0.6 : ci#=0.6                   ;Cantor-Staub
   ;c#=0.11031 : ci#=0.67037             ;Fatou-Staub
 ;  c#=-0.39054 : ci#=0.58679             ;Siegel-Disk

	maxx%=GraphicsWidth() : maxy%=GraphicsHeight()
	xoff#=0 : yoff#=0 : zoom# = 0.8
	
	maxit%=200 : ra# = 10000;199715970000
End Function

Function transform#(x#,y#,c#,ci#,maxit%) ;Fraktalberechnung
	it%=0
	Repeat
		x1#=x#^2-y#^2+c# : y1#=2*x#*y#+ci#
		distance#=Sqr(x1#^2+y2#^2)
		x#=x1# : y#=y1# : it%=it%+1
	Until it%>=maxit% Or distance#>=ra#
	
	Return Float(it%)/Float(maxit%)
	
End Function

Function control() ;Programmsteuerung Tastatur, Maus
	If KeyHit(57) Then bit1%=1-bit1%
	
	zoom#=zoom#*(2^(KeyHit(78)-KeyHit(74)))
	xoff#=xoff#+(KeyHit(203)-KeyHit(205))*0.2/zoom#
	yoff#=yoff#+(KeyHit(200)-KeyHit(208))*0.2/zoom#
	
	If MouseHit(1) Then
		xoff#=xoff#-(2*Float(MouseX()-maxx/2)/maxy)/zoom#
		yoff#=yoff#-(2*Float(MouseY()-maxy/2)/maxy)/zoom#
		zoom#=zoom#*2
	End If
	
	If MouseHit(2) Then   
		zoom#=zoom#/2
		xoff#=xoff#+(Float(MouseX()-maxx/2)/maxy)/zoom#
		yoff#=yoff#+(Float(MouseY()-maxy/2)/maxy)/zoom#
	End If
	
	maxit%=maxit%+10*(KeyHit(201)-KeyHit(209))
	
	ci#=ci#+0.02*(KeyHit(17)-KeyHit(31))
	c#=c#+0.02*(KeyHit(32)-KeyHit(30))   
	If KeyHit(14) Then initvalues()
	If KeyHit(1) Then End
End Function

Function rgbfade(value#) ;Farbverlauf erzeugen aus Zahl zwischen 0 und 1
	If value#=1 Then Return 0
	
	value#=value#*7
	
	r#=Abs(-Abs(-value#+1.5)+2.5)-1: r# = (r#>0)*(r#<1)*r# + (r#>=1)
	g#=-Abs(Abs(5.5-value#)-2.5)+2:  g# = (g#>0)*(g#<1)*g# + (g#>=1)
	b#=value#-3:                     b# = (b#>0)*(b#<1)*b# + (b#>=1)

	;r#=Abs(-Abs(-value#+1.5)+1.5)-1: r# = (r#>0)*(r#<1)*r# + (r#>=1)
	;g#=-Abs(Abs(5.5-value#)-2.5)+2:  g# = (g#>0)*(g#<1)*g# + (g#>=1)
	;b#=value#-3:                     b# = (b#>0)*(b#<1)*b# + (b#>=1)
		
	Return Int(r#*255)*256^2+Int(g#*255)*256+Int(b#*255)
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D