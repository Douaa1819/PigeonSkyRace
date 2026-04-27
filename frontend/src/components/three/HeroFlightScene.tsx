import { Canvas, useFrame } from '@react-three/fiber';
import { Points, PointMaterial, Float, MeshDistortMaterial } from '@react-three/drei';
import { Suspense, useMemo, useRef } from 'react';
import * as THREE from 'three';

const PARTICLE_COUNT = 10000;

function insideMoroccoLikeShape(x: number, y: number) {
  const body = x > -1.05 && x < 0.92 && y > -1.16 && y < 1.12;
  const atlasCut = y > x * 0.78 - 0.35;
  const coastCurve = y < 1.18 - Math.pow(x + 0.42, 2) * 0.9;
  const southTail = y > -1.28 + (x + 0.1) * -0.2;
  const eastEdge = x < 0.95 - Math.pow(y - 0.15, 2) * 0.32;
  return body && atlasCut && coastCurve && southTail && eastEdge;
}

function ParticleFlock() {
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
      const z = topo + (Math.random() - 0.5) * 0.08;
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
    const blue = new THREE.Color('#2563EB');
    const gold = new THREE.Color('#FACC15');
    for (let i = 0; i < PARTICLE_COUNT; i += 1) {
      const mix = Math.random();
      const col = blue.clone().lerp(gold, mix > 0.78 ? 1 : mix * 0.35);
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
    const rippleCenterX = pointer.x * 1.4;
    const rippleCenterY = pointer.y * 1;

    for (let i = 0; i < PARTICLE_COUNT; i += 1) {
      const ix = i * 3;
      const x = pos[ix];
      const y = pos[ix + 1];
      const z = pos[ix + 2];

      const targetY = Math.sin(x * 0.8 + t * 0.8) * 0.035 + y * 0.996;
      const targetZ = Math.cos(x * 1.35 - t * 0.62) * 0.06;
      const dx = x - rippleCenterX;
      const dy = y - rippleCenterY;
      const dist = Math.sqrt(dx * dx + dy * dy);
      const ripple = Math.exp(-dist * 1.6) * Math.sin(t * 6.2 - dist * 6.5) * 0.016;

      velocities[ix] += (pointer.x * 0.14 - x * 0.012) * 0.0008;
      velocities[ix + 1] += (targetY - y) * 0.0018;
      velocities[ix + 2] += (targetZ + ripple - z) * 0.0019;

      pos[ix] += velocities[ix];
      pos[ix + 1] += velocities[ix + 1];
      pos[ix + 2] += velocities[ix + 2];

      velocities[ix] *= 0.985;
      velocities[ix + 1] *= 0.985;
      velocities[ix + 2] *= 0.985;
    }

    pointsRef.current.rotation.y = t * 0.08 + pointer.x * 0.28;
    pointsRef.current.rotation.x = pointer.y * 0.18;
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

function GodRays() {
  return (
    <group>
      <mesh rotation={[-0.9, 0.2, 0]} position={[0.8, 1.45, 0]}>
        <coneGeometry args={[0.7, 2.9, 24, 1, true]} />
        <meshBasicMaterial color="#22D3EE" transparent opacity={0.1} blending={THREE.AdditiveBlending} />
      </mesh>
      <mesh rotation={[-2.2, 0.3, 0]} position={[-0.9, -1.1, 0.4]}>
        <coneGeometry args={[0.55, 2.2, 24, 1, true]} />
        <meshBasicMaterial color="#F59E0B" transparent opacity={0.08} blending={THREE.AdditiveBlending} />
      </mesh>
    </group>
  );
}

export function HeroFlightScene() {
  return (
    <div className="hero-scene">
      <Canvas camera={{ position: [0, 0.15, 3.35], fov: 45 }} dpr={[1, 1.7]}>
        <Suspense fallback={null}>
          <fog attach="fog" args={['#020203', 3.6, 8.4]} />
          <ambientLight intensity={0.08} color="#0EA5E9" />
          <directionalLight position={[0.2, 2.5, 1.2]} intensity={1.2} color="#22D3EE" />
          <pointLight position={[0, -1.8, -0.5]} intensity={0.9} color="#FFD700" />

          <Float speed={1.2} rotationIntensity={0.14} floatIntensity={0.2}>
            <ParticleFlock />
          </Float>
          <GodRays />
          <mesh position={[0, -0.95, -0.4]}>
            <torusGeometry args={[1.7, 0.18, 18, 64]} />
            <MeshDistortMaterial color="#B9C5D4" distort={0.36} speed={1.5} transparent opacity={0.14} />
          </mesh>
        </Suspense>
      </Canvas>
    </div>
  );
}
