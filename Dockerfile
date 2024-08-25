# Use the Python 3.8 image
FROM python:3.8

# Expose port 8000
EXPOSE 8000

# Create and set working directory
RUN mkdir /app
WORKDIR /app

# Copy the local code to the container
COPY . /app

# Install Python dependencies
RUN pip install -r requirements.txt

# Set the default command to run the application
ENTRYPOINT ["fastapi", "dev", "/app/application.py", "--host", "0.0.0.0"]
