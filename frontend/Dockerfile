# Stage 1: Build the React app with Vite
FROM node:18 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json (if available)
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application source code
COPY . .

# Build the application
RUN npm run build

# Install `serve` to serve the built files
RUN npm install -g serve

# Expose the port the app runs on
EXPOSE 3000

# Start the built application using `serve`
CMD ["serve", "-s", "dist"]
