		byte[] buffer = new byte[6];
		
		wire.transaction(null, 0, buffer, buffer.length);
		
		byte[] x = new byte[2];
		byte y = 0;
		byte heightValue = 0;
		byte[] widthValue = new byte[2];
		
		for(int i = 0; i < buffer.length; i++) {
		
			if(i == 0) {
				y = buffer[i];
			}
			else if(i == 1 || i == 2) {
				x[i - 1] = buffer[i];
			}
			else if(i == 3) {
				heightValue = buffer[i];
			}
			else {
				widthValue[i - 4] = buffer[i];
			}
			
		}
		
		int yCoord = (int) y & 0xFF;
		int x1 = (int) (x[0] & 0xFF);
		int x2 = (int) ((x[1] & 0xFF) * 256);
		int xCoord = x1 + x2;
		int w1 = (int) (widthValue[0] & 0xFF);
		int w2 = (int) ((widthValue[1] & 0xFF) * 256);
		int width = w2 + w1;
		int height = (int) heightValue & 0xFF;

		System.out.println("X: " + xCoord + " Y: " + yCoord + " Width: " + width + " Height: " + height);
