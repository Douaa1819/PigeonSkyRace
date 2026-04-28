import { Canvas, useFrame } from '@react-three/fiber';
import { Points, PointMaterial, Float, MeshDistortMaterial, Line } from '@react-three/drei';
import { Suspense, useMemo, useRef } from 'react';
import * as THREE from 'three';
import type { MotionValue } from 'framer-motion';

const PARTICLE_COUNT = 10000;

function insideMoroccoLikeShape(x: number, y: number) {
  const body = x > -1.05 && x < 0.92 && y > -1.16 && y < 1.12;
  const atlasCut = y > x * 0.78 - 0.35;
  const coastCurve = y < 1.18 - Math.pow(x + 0.42, 2) * 0.9;
  const southTail = y > -1.28 + (x + 0.1) * -0.2;
  const eastEdge = x < 0.95 - Math.pow(y - 0.15, 2) * 0.32;
  return body && atlasCut && coastCurve && southTail && eastEdge;
}

function ParticleFlock({ scrollProgress }: { scrollProgress?: MotionValue<number> }) {
  const pointsRef = useRef<THREE.Points>(null);
  const velocities = useMemo(() => new Float32Array(PARTICLE_COUNT * 3), []);

  const positions = useMemo(() => {
    const p = new Float32Array(PARTICLE_COUNT * 3);
    for (let i = 0; i < PARTICLE_COUNT; i += 1) {
      let x = 0;
      let y = 0;
      do {
        x = (Math.random() - 0.5) * 2.2;
        y = (Math.random() - 0.5) * 2.4;
      } while (!insideMoroccoLikeShape(x, y));

      const topo = Math.sin((x + 0.6) * 4.2) * 0.05 + Math.cos((y - 0.18) * 5.4) * 0.04;
      const z = -0.55 + topo + (Math.random() - 0.5) * 0.12;
      p[i * 3] = x * 1.4;
      p[i * 3 + 1] = y * 1.2;
      p[i * 3 + 2] = z;

      velocities[i * 3] = (Math.random() - 0.5) * 0.002;
      velocities[i * 3 + 1] = (Math.random() - 0.5) * 0.002;
      velocities[i * 3 + 2] = (Math.random() - 0.5) * 0.002;
    }
    return p;
  }, [velocities]);

  const colors = useMemo(() => {
    const c = new Float32Array(PARTICLE_COUNT * 3);
    const silver = new THREE.Color('#D1D5DB');
    const white = new THREE.Color('#FFFFFF');
    const darkSilver = new THREE.Color('#9CA3AF');
    for (let i = 0; i < PARTICLE_COUNT; i += 1) {
      const mix = Math.random();
      const col = mix > 0.55 ? silver.clone().lerp(white, mix * 0.65) : darkSilver.clone().lerp(silver, mix * 0.9);
      c[i * 3] = col.r;
      c[i * 3 + 1] = col.g;
      c[i * 3 + 2] = col.b;
    }
    return c;
  }, []);

  useFrame(({ clock, pointer }) => {
    if (!pointsRef.current) return;
    const pos = pointsRef.current.geometry.attributes.position.array as Float32Array;
    const t = clock.elapsedTime;
    const progress = scrollProgress?.get() ?? 0;
    const burst = 1 + progress * 10;
    const rippleCenterX = pointer.x * 1.4;
    const rippleCenterY = pointer.y * 1;

    for (let i = 0; i < PARTICLE_COUNT; i += 1) {
      const ix = i * 3;
      const x = pos[ix];
      const y = pos[ix + 1];
      const z = pos[ix + 2];

      const targetY = Math.sin(x * 0.8 + t * 0.8 * burst) * 0.03 + y * (0.995 - progress * 0.0015);
      const targetZ = Math.cos(x * 1.35 - t * 0.62 * burst) * (0.05 + progress * 0.04);
      const dx = x - rippleCenterX;
      const dy = y - rippleCenterY;
      const dist = Math.sqrt(dx * dx + dy * dy);
      const ripple = Math.exp(-dist * 1.6) * Math.sin(t * (5.6 + burst) - dist * 6.5) * (0.012 + progress * 0.03);

      velocities[ix] += (pointer.x * 0.14 - x * 0.012) * (0.0008 + progress * 0.0008);
      velocities[ix + 1] += (targetY - y) * (0.0017 + progress * 0.0016);
      velocities[ix + 2] += (targetZ + ripple - z) * (0.0019 + progress * 0.0034);
      if (progress > 0.15) {
        velocities[ix + 2] -= (0.0006 + progress * 0.0026);
      }

      pos[ix] += velocities[ix];
      pos[ix + 1] += velocities[ix + 1];
      pos[ix + 2] += velocities[ix + 2];

      velocities[ix] *= 0.984 - Math.min(progress * 0.01, 0.006);
      velocities[ix + 1] *= 0.984 - Math.min(progress * 0.01, 0.006);
      velocities[ix + 2] *= 0.983 - Math.min(progress * 0.012, 0.008);
    }

    pointsRef.current.rotation.y = t * 0.06 + pointer.x * 0.2;
    pointsRef.current.rotation.x = pointer.y * 0.1;
    pointsRef.current.geometry.attributes.position.needsUpdate = true;
  });

  return (
    <Points ref={pointsRef} positions={positions} colors={colors} stride={3}>
      <PointMaterial
        size={0.0125}
        vertexColors
        transparent
        opacity={0.92}
        blending={THREE.AdditiveBlending}
        depthWrite={false}
        sizeAttenuation
      />
    </Points>
  );
}

function RacingPigeon({ scrollProgress }: { scrollProgress?: MotionValue<number> }) {
  const bird = useRef<THREE.Group>(null);
  const leftWing = useRef<THREE.Mesh>(null);
  const rightWing = useRef<THREE.Mesh>(null);

  useFrame(({ clock }) => {
    if (!bird.current || !leftWing.current || !rightWing.current) return;
    const progress = scrollProgress?.get() ?? 0;
    const flapRate = 2.2 + progress * 14;
    const flap = Math.sin(clock.elapsedTime * flapRate) * (0.45 + progress * 0.2);
    leftWing.current.rotation.z = -0.5 + flap;
    rightWing.current.rotation.z = 0.5 - flap;
    bird.current.position.z = 0.12 + progress * 0.18;
    bird.current.position.y = 0.1 + Math.sin(clock.elapsedTime * 0.7) * 0.05;
  });

  return (
    <group ref={bird} position={[0, 0.1, 0.14]}>
      <mesh castShadow receiveShadow>
        <sphereGeometry args={[0.22, 18, 18]} />
        <meshPhysicalMaterial color="#d1d5db" metalness={0.95} roughness={0.16} clearcoat={1} clearcoatRoughness={0.18} />
      </mesh>
      <mesh position={[0.28, 0.05, 0]}>
        <sphereGeometry args={[0.1, 16, 16]} />
        <meshPhysicalMaterial color="#f8fafc" metalness={0.9} roughness={0.2} transmission={0.25} thickness={0.8} />
      </mesh>
      <mesh ref={leftWing} position={[-0.18, 0.03, 0]} rotation={[0.08, 0.15, -0.5]}>
        <boxGeometry args={[0.48, 0.05, 0.2]} />
        <meshPhysicalMaterial color="#9ca3af" metalness={0.88} roughness={0.22} />
      </mesh>
      <mesh ref={rightWing} position={[-0.18, 0.03, 0]} rotation={[0.08, -0.15, 0.5]}>
        <boxGeometry args={[0.48, 0.05, 0.2]} />
        <meshPhysicalMaterial color="#9ca3af" metalness={0.88} roughness={0.22} />
      </mesh>
      <mesh position={[-0.36, 0.04, 0]}>
        <coneGeometry args={[0.07, 0.26, 12]} />
        <meshPhysicalMaterial color="#cbd5e1" metalness={0.9} roughness={0.25} />
      </mesh>
    </group>
  );
}

function GodRays() {
  return (
    <group>
      <mesh rotation={[-0.9, 0.2, 0]} position={[0.8, 1.45, 0]}>
        <coneGeometry args={[0.7, 2.9, 24, 1, true]} />
        <meshBasicMaterial color="#d1d5db" transparent opacity={0.08} blending={THREE.AdditiveBlending} />
      </mesh>
      <mesh rotation={[-2.2, 0.3, 0]} position={[-0.9, -1.1, 0.4]}>
        <coneGeometry args={[0.55, 2.2, 24, 1, true]} />
        <meshBasicMaterial color="#9ca3af" transparent opacity={0.07} blending={THREE.AdditiveBlending} />
      </mesh>
    </group>
  );
}

function GeoGrid() {
  return (
    <group position={[1.8, -0.25, -0.6]} rotation={[0, -0.35, 0]}>
      {Array.from({ length: 7 }).map((_, i) => (
        <Line
          key={`lat-${i}`}
          points={[
            [-1.1, -0.78 + i * 0.26, 0],
            [1.2, -0.78 + i * 0.26, 0],
          ]}
          color="#6b7280"
          lineWidth={0.55}
          transparent
          opacity={0.18}
        />
      ))}
      {Array.from({ length: 8 }).map((_, i) => (
        <Line
          key={`lon-${i}`}
          points={[
            [-0.95 + i * 0.3, -0.9, 0],
            [-0.95 + i * 0.3, 0.86, 0],
          ]}
          color="#6b7280"
          lineWidth={0.55}
          transparent
          opacity={0.18}
        />
      ))}
    </group>
  );
}

export function HeroFlightScene({ scrollProgress }: { scrollProgress?: MotionValue<number> }) {
  return (
    <div className="hero-scene">
      <Canvas camera={{ position: [0, 0.15, 3.35], fov: 45 }} dpr={[1, 1.7]}>
        <Suspense fallback={null}>
          <fog attach="fog" args={['#000000', 3.6, 8.4]} />
          <ambientLight intensity={0.12} color="#9ca3af" />
          <spotLight
            position={[0.15, 1.2, 1.35]}
            angle={0.45}
            penumbra={0.5}
            intensity={2.2}
            color="#f8fafc"
            castShadow
          />
          <pointLight position={[0, -1.1, 0.5]} intensity={0.5} color="#d1d5db" />

          <Float speed={1.2} rotationIntensity={0.14} floatIntensity={0.2}>
            <ParticleFlock scrollProgress={scrollProgress} />
          </Float>
          <RacingPigeon scrollProgress={scrollProgress} />
          <GeoGrid />
          <GodRays />
          <group position={[2.15, 0.95, -0.4]}>
            <Line points={[[-0.35, 0, 0], [0.35, 0, 0]]} color="#9ca3af" lineWidth={0.8} />
            <Line points={[[0, -0.32, 0], [0, 0.32, 0]]} color="#9ca3af" lineWidth={0.8} />
          </group>
          <mesh position={[0, -0.95, -0.4]}>
            <torusGeometry args={[1.7, 0.18, 18, 64]} />
            <MeshDistortMaterial color="#9ca3af" distort={0.36} speed={1.5} transparent opacity={0.12} />
          </mesh>
        </Suspense>
      </Canvas>
      <div className="hero-coordinates">
        <span>34.02° N</span>
        <span>6.83° W</span>
      </div>
    </div>
  );
}
