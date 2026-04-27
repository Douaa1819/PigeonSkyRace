import { Canvas, useFrame } from '@react-three/fiber';
import { Float, Environment, MeshDistortMaterial } from '@react-three/drei';
import { useMemo, useRef } from 'react';
import type { Group, Mesh, MeshBasicMaterial } from 'three';

function FlightPigeon() {
  const group = useRef<Group>(null);
  const leftWing = useRef<Mesh>(null);
  const rightWing = useRef<Mesh>(null);

  useFrame(({ clock, pointer }) => {
    const t = clock.elapsedTime;
    if (!group.current || !leftWing.current || !rightWing.current) return;

    group.current.rotation.y = pointer.x * 0.3 + Math.sin(t * 0.35) * 0.1;
    group.current.rotation.x = -0.05 + pointer.y * 0.14;
    group.current.position.y = Math.sin(t * 0.9) * 0.12;
    group.current.position.x = Math.sin(t * 0.22) * 0.22;

    const flap = Math.sin(t * 3.2) * 0.4;
    leftWing.current.rotation.z = -0.3 + flap;
    rightWing.current.rotation.z = 0.3 - flap;
  });

  return (
    <group ref={group} position={[0, 0, 0]}>
      <mesh castShadow receiveShadow>
        <sphereGeometry args={[0.48, 24, 24]} />
        <meshStandardMaterial color="#E5E7EB" roughness={0.28} metalness={0.08} />
      </mesh>

      <mesh position={[0.46, 0.04, 0]} castShadow>
        <sphereGeometry args={[0.2, 20, 20]} />
        <meshStandardMaterial color="#D1D5DB" roughness={0.3} metalness={0.08} />
      </mesh>

      <mesh ref={leftWing} position={[-0.45, 0.02, 0]} rotation={[0, 0, -0.25]} castShadow>
        <boxGeometry args={[0.85, 0.08, 0.32]} />
        <meshStandardMaterial color="#9CA3AF" roughness={0.42} />
      </mesh>
      <mesh ref={rightWing} position={[-0.45, 0.02, 0]} rotation={[0, 0, 0.25]} castShadow>
        <boxGeometry args={[0.85, 0.08, -0.32]} />
        <meshStandardMaterial color="#9CA3AF" roughness={0.42} />
      </mesh>

      <mesh position={[-0.72, 0.02, 0]}>
        <coneGeometry args={[0.18, 0.65, 16]} />
        <meshStandardMaterial color="#64748B" roughness={0.44} />
      </mesh>
    </group>
  );
}

function FlightTrails() {
  const meshes = useRef<Mesh[]>([]);
  const materials = useRef<MeshBasicMaterial[]>([]);
  const positions = useMemo(
    () => Array.from({ length: 12 }, (_, i) => [-1.2 - i * 0.26, Math.sin(i * 0.45) * 0.25, -0.25]),
    []
  );

  useFrame(({ clock }) => {
    const t = clock.elapsedTime;
    meshes.current.forEach((mesh, i) => {
      if (!mesh) return;
      mesh.position.y = Math.sin(t * 1.8 + i * 0.5) * 0.2;
      if (materials.current[i]) {
        materials.current[i].opacity = 0.14 + (Math.sin(t * 1.2 + i) + 1) * 0.1;
      }
      mesh.scale.setScalar(0.7 + ((Math.sin(t * 2.1 + i) + 1) * 0.35) / 2);
    });
  });

  return (
    <group>
      {positions.map((pos, i) => (
        <mesh key={i} position={pos as [number, number, number]} ref={(el) => (meshes.current[i] = el as Mesh)}>
          <sphereGeometry args={[0.07, 12, 12]} />
          <meshBasicMaterial
            color="#38BDF8"
            transparent
            opacity={0.2}
            ref={(el) => (materials.current[i] = el as MeshBasicMaterial)}
          />
        </mesh>
      ))}
    </group>
  );
}

export function HeroFlightScene() {
  return (
    <div className="hero-scene">
      <Canvas camera={{ position: [0, 0.35, 3.1], fov: 45 }} dpr={[1, 1.5]}>
        <ambientLight intensity={0.65} color="#9DD6FF" />
        <directionalLight position={[2.2, 2.5, 2]} intensity={1} color="#38BDF8" castShadow />
        <pointLight position={[-2, -1, 2]} intensity={0.4} color="#2563EB" />
        <Environment preset="city" />

        <Float speed={1.8} rotationIntensity={0.25} floatIntensity={0.55}>
          <FlightPigeon />
        </Float>
        <FlightTrails />
        <mesh position={[0, -0.95, -0.4]}>
          <torusGeometry args={[1.7, 0.2, 18, 64]} />
          <MeshDistortMaterial color="#1D4ED8" distort={0.25} speed={2.2} transparent opacity={0.2} />
        </mesh>
      </Canvas>
    </div>
  );
}
